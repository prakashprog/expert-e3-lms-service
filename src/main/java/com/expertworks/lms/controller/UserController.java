package com.expertworks.lms.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import com.expertworks.lms.enums.ExpertRole;
import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.http.EmailDTO;
import com.expertworks.lms.http.TransferDTO;
import com.expertworks.lms.http.UserDTO;
import com.expertworks.lms.model.Group;
import com.expertworks.lms.model.Partner;
import com.expertworks.lms.model.Team;
import com.expertworks.lms.model.User;
import com.expertworks.lms.repository.GroupRepository;
import com.expertworks.lms.repository.PartnerRepository;
import com.expertworks.lms.repository.TeamRepository;
import com.expertworks.lms.repository.UserRepository;
import com.expertworks.lms.service.EmailService;
import com.expertworks.lms.service.TeamCoursesService;
import com.expertworks.lms.service.TeamService;
import com.expertworks.lms.util.TokenUtil;

//https://stackoverflow.com/questions/16232833/how-to-respond-with-http-400-error-in-a-spring-mvc-responsebody-method-returnin

@RestController
@Component
public class UserController {

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);

	public static final String SUCCESS = "success";
//	public static final String ROLE_ADMIN = "ROLE_ADMIN";
//	public static final String ROLE_USER = "ROLE_USER";
//	public static final String ROLE_PUBUSER = "ROLE_PUBUSER";
//	public static final String ROLE_SUPERADMIN = "ROLE_SUPERADMIN";
	public static final String ACTION_DELETE = "DELETE";
	public static final String TEAM_B2C = "TEAM_B2C";

	public static final String USER_NOTVERIFIED = "NOTVERIFIED";
	public static final String USER_VERIFIED = "VERIFIED";

	@Autowired
	private EmailService emailService;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private TeamService teamService;

	@Autowired
	private PartnerRepository partnerRepository;

	@Autowired
	TokenUtil tokenUtil;

	@Autowired
	private TeamCoursesService teamCoursesService;

	@Value("${email.verify.sucessurl}")
	private String emailSuccess;

	@Value("${email.verify.failureurl}")
	private String emailFailed;

	@Value("${app.admin.user.limit}")
	private String adminUserLimitProp;

	@GetMapping("/")
	public String home() {
		return "Server is running...";
	}

	@CrossOrigin
	@PostMapping("/public/signup")
	public ApiResponse createSignUp(@RequestBody User user) throws Exception {

		User savedUser = null;
		savedUser = userRepository.load(user.getEmail());
		if (savedUser != null)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, user.getEmail() + " already exists ");

		user.setUserId(user.getEmail());
		user.setUserName(user.getName());
		// unique TeamId for each signup
		user.setTeamId(TEAM_B2C + "-" + user.getEmail());
		user.setStatus(USER_NOTVERIFIED);
		user.setUserRole(ExpertRole.ROLE_PUBUSER);
		user.setEnabled(false);// By default user is disabled
		user.setStatus(USER_NOTVERIFIED);
		savedUser = userRepository.save(user);

		/*
		 * emailService.sendEmailVerification(user.getEmail(),
		 * StringUtils.capitalize(user.getName()), user.getUserId(),
		 * savedUser.getVcode());
		 */

		EmailDTO email = new EmailDTO();
		email.to = user.getEmail();
		email.username = StringUtils.capitalize(user.getName());
		email.loginId = user.getUserId();
		email.verificationKey = savedUser.getVcode();

		emailService.sendEmailVerificationv1(email);

		return new ApiResponse(HttpStatus.OK, SUCCESS, savedUser);
	}

	@CrossOrigin
	@GetMapping("/public/sigsnup/{userId}/{verificationtoken}")
	public RedirectView verifyEmail(@PathVariable("userId") String userId,
			@PathVariable("verificationtoken") String verificationtoken) throws Exception {

		logger.info("verificationtoken:" + verificationtoken + "; userId : " + userId);
		User savedUser = userRepository.load(userId);

		logger.info("savedUser.vcode :" + savedUser.getVcode());
		RedirectView redirectView = new RedirectView();

		if (savedUser.getVcode().equals(verificationtoken)) {
			System.out.println("emailSuccess :" + emailSuccess);
			redirectView.setUrl("https://www.expert-works.com");
			savedUser.setEnabled(true);
			savedUser.setStatus(USER_VERIFIED);
			userRepository.update(userId, savedUser);

		} else
			redirectView.setUrl("https://www.expert-works.com/error");

		return redirectView;
	}

	@CrossOrigin
	@GetMapping("/to-be-redirected")
	public ResponseEntity<Object> redirectToExternalUrl() throws URISyntaxException {
		URI yahoo = new URI("http://www.yahoo.com");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(yahoo);
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

	@CrossOrigin
	@PostMapping("/user/transfer/{userId}")
	public ApiResponse transferUser(@PathVariable("userId") String userId, @RequestBody TransferDTO transferDTO)
			throws Exception {

		User user = userRepository.load(userId);
		if (user.getTeamId().equals(transferDTO.getFromTeam())) {
			Team team = teamRepository.load(transferDTO.getToTeam(), "details");
			user.setTeamId(team.getTeamId());
			user = userRepository.update(userId, user);
		} else
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
		return new ApiResponse(HttpStatus.OK, SUCCESS, user);

	}

	/*
	 * Adding user under the Team.
	 */
	@CrossOrigin
	@PostMapping("/user/{teamId}")
	public ApiResponse createUserUnderTeam(@PathVariable("teamId") String teamId, @RequestBody User user)
			throws Exception {

		int userLimitDB = 4;// setting to default; ideally should come from DB
		int currentUserCount;
		user.setUserId(user.getEmail());
		user.setUserName(user.getName());
		user.setTeamId(teamId);

		if (user.getUserRole() == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userRole is missing");
		// List<Team> teamList = teamRepository.get(teamId); // check for team exists
		// Team team = teamList.get(0);
		checkforDuplicateUser(user.getEmail());

		Team teamInDB = teamRepository.load(teamId, "details");

		if (teamInDB == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team is missing");
		System.out.println("Team from DB : " + teamInDB.getTeamId());
		// Group group = groupRepository.queryOnsk(team).get(0);
		Group group = groupRepository.load(teamInDB.getGroupId(), "details");
		if (group == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Group is missing");
		System.out.println("Group from DB : " + group.getGroupId());
		if (group.getUserLimit() == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Limit on Group " + teamInDB.getGroupId() + " is missing");
		System.out.println("getUserLimit in GroupTable from DB : " + group.getUserLimit());
		int groupUserLimit = Integer.parseInt(group.getUserLimit());

		userLimitDB = groupUserLimit;
		// List teamUsers = userRepository.queryOnGSI("teamId-index", "teamId", teamId);
		// userCount = teamUsers.size();

		List<User> groupUsers = userRepository.queryOnGSI("groupId-index", "groupId", teamInDB.getGroupId());
		currentUserCount = groupUsers.size();

		logger.info("Current userCount in DB for Team :" + currentUserCount + "(TeamId :" + teamId + ")");
		if (currentUserCount >= userLimitDB) {
			// throw new Exception("Maximum user count reached");
//			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
//					"Group Users Reached Maximun limit : " + userLimitDB  +",currentUserCountinDB: "+currentUserCount+";groupId:"+teamInDB.getGroupId());
//
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"You have reached the limit of Maximum number of licenses. : " + userLimitDB);
		}

		user.setGroupId(group.getGroupId());
		// review once zone
		Partner partner = partnerRepository.load(group.getPartnerId(), "details");
		// Partner partner = partnerRepository.queryOnsk(group.getGroupId()).get(0);
		user.setPartnerImg(partner.getImg());
		user.setPartnerId(partner.getPartnerId());
		user.setUserLimit(String.valueOf(userLimitDB));

		if (user.getUserRole() != null && user.getUserRole() == ExpertRole.ROLE_ADMIN) {
			user.setPassword("admin");// default password
		} else {
			String randompwd = this.getRandomPassword(4);
			user.setPassword(randompwd);
		}
		User savedUser = userRepository.save(user);
		teamRepository.addUserToTeam(teamInDB.getTeamId(), savedUser);
//		emailService.sendCredentailsMessagev1(user.getEmail(), StringUtils.capitalize(user.getName()), user.getUserId(),
//				user.getPassword());

		EmailDTO email = new EmailDTO();
		email.to = user.getEmail();
		email.username = StringUtils.capitalize(user.getName());
		email.loginId = user.getUserId();
		email.password = user.getPassword();
		emailService.sendCredentailsMessagev1(email);

		return new ApiResponse(HttpStatus.OK, SUCCESS, savedUser);

	}

	/*
	 * Adding user under the Group(only Administration(admin) Users)
	 */
	@CrossOrigin
	@PostMapping("/user")
	public ApiResponse createUser(@RequestParam(required = true, name = "groupId") String groupId,
			@RequestParam(required = false, name = "teamId") String teamId, @RequestBody User user) throws Exception {

		String partnerId;
		int adminUserLimit = Integer.parseInt(adminUserLimitProp);// setting to default; ideally should come from DB
		int currentUserCountinDB;
		user.setUserId(user.getEmail());
		user.setUserName(user.getName());
		user.setTeamId(teamId);
		if (user.getUserRole() == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userRole is missing");

		checkforDuplicateUser(user.getEmail());
		if (user.getUserRole() != null && user.getUserRole() == ExpertRole.ROLE_ADMIN) {
			Team adminTeam = teamService.createOrGetAdminTeam(groupId);
			teamId = adminTeam.getTeamId();
			System.out.println("createUser  TeamId : " + teamId);
			user.setTeamId(teamId);
			user.setTeamType(adminTeam.getTeamType());
		}
		if (teamId != null) {
			Team team = teamRepository.load(teamId, "details");
			if (team == null)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team is missing");
			System.out.println("Team from DB : " + team.getTeamId());
		}

		Group group = groupRepository.load(groupId, "details");
		if (group == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Group is missing");
		System.out.println("Group from DB : " + group.getGroupId());
		if (group.getUserLimit() == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit on Group " + groupId + " is missing");
		System.out.println("getUserLimit in GroupTable from DB : " + group.getUserLimit());
		int groupUserLimit = Integer.parseInt(group.getUserLimit());
		// adminUserLimit = groupUserLimit;
		List<User> groupUsers = userRepository.queryOnGSI("groupId-index", "groupId", groupId);
		List<User> adminUsers = groupUsers.stream().filter(u -> u.getUserRole().equals(ExpertRole.ROLE_ADMIN))
				.collect(Collectors.toList());
		// userCount = groupUsers.size();
		currentUserCountinDB = adminUsers.size();
		logger.info("Current userCount in DB for Group :" + currentUserCountinDB + "(groupId :" + groupId + ")");
		if (currentUserCountinDB >= adminUserLimit) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin Users Reached Maximun limit : "
					+ adminUserLimit + ",currentUserCountinDB: " + currentUserCountinDB);
		}
		user.setGroupId(group.getGroupId());
		partnerId = group.getPartnerId();
		Partner partner = partnerRepository.load(partnerId, "details");
		// Partner partner = partnerRepository.queryOnsk(group.getGroupId()).get(0);
		// partner.getImg()
		user.setPartnerImg(partner.getImg());
		user.setPartnerId(partner.getPartnerId());
		user.setUserLimit(String.valueOf(adminUserLimit));

		if (user.getUserRole() != null && user.getUserRole() == (ExpertRole.ROLE_ADMIN)) {
			user.setPassword("admin");// default password
			teamCoursesService.addAllCourses(teamId, groupId, partnerId);

		} else {
			String randompwd = this.getRandomPassword(4);
			user.setPassword(randompwd);
		}
		User savedUser = userRepository.save(user);
		// teamRepository.addUserToTeam(team.getTeamId(), savedUser);
		EmailDTO email = new EmailDTO(user.getEmail(), StringUtils.capitalize(user.getName()), user.getUserId(),
				user.getPassword());
		emailService.sendCredentailsMessagev1(email);
		return new ApiResponse(HttpStatus.OK, SUCCESS, savedUser);

	}

//	@CrossOrigin
//	@PostMapping("/user") // deprecated
//
//	public ApiResponse createUserUnderPartner(@RequestBody User user) throws Exception {
//
//		user.setUserName(user.getName());
//		user.setUserId(user.getEmail());
//		user.setTeamId("teamId5"); // Default team Id
//		User savedUser = null;
//		String partnerId = null;
//		partnerId = tokenUtil.getPartner();
//		System.out.println("partnerId for User : " + partnerId);
//		List<Partner> partnerList = partnerRepository.get(partnerId);
//		if (partnerList != null && partnerList.size() > 0) {
//			Partner partner = partnerList.get(0);
//			user.setPartnerId(partner.getPartnerId());
//			user.setUserRole(ROLE_USER);
//
//			User existingUser = userRepository.load(user.getUserId());
//			if (existingUser != null) {
//				throw new Exception("User already present with id : " + user.getUserId());
//			}
//
//			savedUser = userRepository.save(user);
//			emailService.sendCredentailsMessage(user.getEmail(), StringUtils.capitalize(user.getName()),
//					user.getUserId(), user.getPassword());
//
//		} else {
//			throw new Exception("Partner not found");
//		}
//
//		return new ApiResponse(HttpStatus.OK, SUCCESS, savedUser.toUserDTO());
//	}

	@CrossOrigin
	@GetMapping("/user") // Get Users for a given Team
	public ApiResponse getAll(@RequestParam(required = false, name = "teamId") String teamId,
			@RequestParam(required = false, name = "groupId") String groupId) {

		List<User> list = null;
		String partnerId = null;
		if (teamId != null && !teamId.equalsIgnoreCase("")) {
			list = userRepository.queryOnGSI("teamId-index", "teamId", teamId);
		} else if (groupId != null && !groupId.equalsIgnoreCase("")) {
			list = userRepository.queryOnGSI("groupId-index", "groupId", groupId);
		} else
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "RequestParam teamId/groupId required");

		Collections.sort(list, new Comparator<User>() {
			@Override
			public int compare(User u1, User u2) {
				// notice the cast to (Integer) to invoke compareTo
				return (u2.getCreatedDate()).compareTo(u1.getCreatedDate());
			}
		});

		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
	}

//	@CrossOrigin
//	@GetMapping("/user")  //Get Users for a given Team
//	public ApiResponse getAll(@RequestParam(required = false, name = "teamId",) String teamId,) {
//
//		List<User> list = null;
//		String partnerId = null;
//		if (teamId == null || teamId.equalsIgnoreCase("")) {
//			partnerId = tokenUtil.getPartner();
//			System.out.println("partnerId : " + partnerId);
//			list = userRepository.queryOnGSI("partnerId-index", "partnerId", partnerId);
//		} else
//			list = userRepository.queryOnGSI("teamId-index", "teamId", teamId);
//		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
//	}

//	@CrossOrigin
//	@GetMapping("/user")  //Get Users for a given Group
//	public ApiResponse getUsersInGroup(@RequestParam(required = false, name = "groupId") String groupId) {
//
//		List<User> list = null;
//		String partnerId = null;
//		if (groupId == null || groupId.equalsIgnoreCase("")) {
//			partnerId = tokenUtil.getPartner();
//			System.out.println("partnerId : " + partnerId);
//			list = userRepository.queryOnGSI("groupId-index", "groupId", groupId);
//		} else
//			list = userRepository.queryOnGSI("groupId-index", "groupId", groupId);
//		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
//	}

	@CrossOrigin
	@GetMapping("/user/{userId}")
	public ApiResponse get(@PathVariable("userId") String userId) {

		UserDTO userDTO = new UserDTO();
		List<User> list = userRepository.get(userId);
		for (User user : list) {
			userDTO.setName(user.getName());
			userDTO.setUserId(user.getUserId());
			userDTO.setGroupId(user.getGroupId());
			userDTO.setTeamId(user.getTeamId());
			userDTO.setPartnerId(user.getPartnerId());
		}

		return new ApiResponse(HttpStatus.OK, SUCCESS, userDTO);

	}

	@CrossOrigin
	@PostMapping("/user/delete")
	public ApiResponse delete(@RequestBody User user, @RequestParam(required = false, name = "action") String action) {
		logger.info("In delete Method");
		return new ApiResponse(HttpStatus.OK, SUCCESS, userRepository.delete(user.getUserId()));

	}

	@CrossOrigin
	@PutMapping("/user/{userId}")
	public ApiResponse update(@PathVariable("userId") String userId, @RequestBody User user) {
		user.setUserId(userId);
		return new ApiResponse(HttpStatus.OK, SUCCESS, userRepository.update(userId, user));
	}

	@CrossOrigin
	@PostMapping("/user/changepwd")
	public ApiResponse changePwd(@RequestBody User user, @RequestParam(required = false, name = "id") String id)
			throws Exception {

		String userId = user.getUserId();
		String newPassword = user.getPassword();
		User loadedUser = userRepository.load(user.getUserId());

		if (loadedUser == null)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, user.getUserId() + " does not exist");

		System.out.println(loadedUser.getUserId());
		System.out.println(loadedUser.getPassword() + ":Id: : " + id);

		System.out.println("DB Password :" + loadedUser.getPassword() + " ;Old Password : " + user.getOldpassword());

		if (!loadedUser.getPassword().equals(user.getOldpassword()) || newPassword == "") {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password did not match");

		}
		user = userRepository.update(userId, user);
		EmailDTO email = new EmailDTO(loadedUser.getEmail(), StringUtils.capitalize(loadedUser.getName()),
				loadedUser.getUserId(), loadedUser.getPassword());
		emailService.sendPasswordChanged(email);

		return new ApiResponse(HttpStatus.OK, SUCCESS, user);
	}

	@CrossOrigin
	@PostMapping("/user/resetpwd")
	public ApiResponse forgotPwd(@RequestBody User user) throws Exception {

		String userId = user.getUserId();
		String randompwd = null;
		User loadedUser = userRepository.load(user.getUserId());
		if (loadedUser == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, userId + " not found");
		}
		System.out.println("In DB userId: " + loadedUser.getUserId() + ", Pwd : " + loadedUser.getPassword());
		randompwd = this.getRandomPassword(4);
		loadedUser.setPassword(randompwd);
		System.out.println("New Pwd: " + randompwd);
		user = userRepository.update(userId, loadedUser);

		EmailDTO email = new EmailDTO();
		email.setTo(loadedUser.getEmail());
		email.setLoginId(loadedUser.getUserId());
		email.setUsername(StringUtils.capitalize(user.getName()));
		email.setPassword(loadedUser.getPassword());
		emailService.sendResetCredentailsEMailv1(email);

		return new ApiResponse(HttpStatus.OK, SUCCESS, user);
	}

	public String getRandomPassword(int len) {
		String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghi" + "jklmnopqrstuvwxyz!@#$%&";
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(chars.charAt(rnd.nextInt(chars.length())));
		return sb.toString();
	}

	public static void main(String[] args) {

		char[] possibleCharacters = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
				.toCharArray();
		String randomStr = RandomStringUtils.random(7, 0, possibleCharacters.length - 1, false, false,
				possibleCharacters, new SecureRandom());
		System.out.println(randomStr);

	}

	@CrossOrigin
	@GetMapping("/geterror")
	public ApiResponse getError() throws Exception {
		int i = 0;
		if (i == 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team Users Reached Maximun limit");
		else
			return new ApiResponse(HttpStatus.OK, SUCCESS, "some response");
	}

	private void checkforDuplicateUser(String userId) {

		User savedUser = userRepository.load(userId);
		if (savedUser != null)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, savedUser.getEmail() + " already exists ");
	}
}
