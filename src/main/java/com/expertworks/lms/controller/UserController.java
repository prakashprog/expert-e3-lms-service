package com.expertworks.lms.controller;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expertworks.lms.http.ApiResponse;
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
import com.expertworks.lms.util.TokenUtil;

@RestController
@Component
public class UserController {

	public static final String SUCCESS = "success";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_MERCHANT = "ROLE_SUPERADMIN";

	@Autowired
	private EmailService emailService;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private PartnerRepository partnerRepository;

	@Autowired
	TokenUtil tokenUtil;

	@CrossOrigin
	@PostMapping("/user/{teamId}")
	public ApiResponse createUserForTeam(@PathVariable("teamId") String teamId, @RequestBody User user) {

		user.setUserName(user.getName());
		user.setTeamId(teamId);
		Group group = groupRepository.queryOnsk(teamId).get(0);
		System.out.println("groupId : " + group.getGroupId());
		user.setGroupId(group.getGroupId());
		Partner partner = partnerRepository.queryOnsk(group.getGroupId()).get(0);
		user.setPartnerId(partner.getPartnerId());
		User savedUser = userRepository.save(user);
		Team team = teamRepository.get(teamId).get(0); // check for existance
		teamRepository.addUserToTeam(team.getTeamId(), savedUser);
		return new ApiResponse(HttpStatus.OK, SUCCESS, savedUser);
	}

	@CrossOrigin
	@PostMapping("/user")
	public ApiResponse createUserForPartner(@RequestBody User user) throws Exception {

		user.setUserName(user.getName());
		User savedUser = null;
		String partnerId = null;
		partnerId = tokenUtil.getPartner();
		System.out.println("partnerId for User : " + partnerId);
		List<Partner> partnerList = partnerRepository.get(partnerId);
		if (partnerList != null && partnerList.size() > 0) {
			Partner partner = partnerList.get(0);
			user.setPartnerId(partner.getPartnerId());
			user.setUserRole(ROLE_USER);
			savedUser = userRepository.save(user);
			emailService.sendCredentailsMessage(user.getEmail(), StringUtils.capitalize(user.getName()),
					user.getUserId(), user.getPassword());

		} else {
			throw new Exception("Partner not found");
		}

		return new ApiResponse(HttpStatus.OK, SUCCESS, savedUser.toUserDTO());
	}

	@CrossOrigin
	@GetMapping("/user")
	public ApiResponse getAll() {

		String partnerId = null;
		partnerId = tokenUtil.getPartner();
		System.out.println("partnerId : " + partnerId);
		List<User> list = userRepository.queryOnGSI("partnerId-index", "partnerId", partnerId);
		 System.out.println("hello : "+ this.getRandomPassword(4));
		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
	}

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
	@DeleteMapping("/user/{userId}")
	public String delete(@PathVariable("userId") String userId) {
		return userRepository.delete(userId);
	}

	@CrossOrigin
	@PutMapping("/user/{userId}")
	public ApiResponse update(@PathVariable("userId") String userId, @RequestBody User user) {
		user.setUserId(userId);
		return new ApiResponse(HttpStatus.OK, SUCCESS, userRepository.update(userId, user));
	}
	
	@CrossOrigin
	@PostMapping("/user/changepwd")
	public ApiResponse changePwd( @RequestBody User user,@RequestParam(required = false, name = "id") String id) throws Exception{
		
		String userId = user.getUserId();
		String newPassword = user.getPassword();
		User loadedUser = userRepository.load(user.getUserId());
		System.out.println(loadedUser.getUserId());
		System.out.println(loadedUser.getPassword()  + ":Id : "+id);
		
		
		System.out.println("DB Password :" +loadedUser.getPassword() + " ;Old Password : "+user.getOldpassword());
	
		if(!loadedUser.getPassword().equals(user.getOldpassword()) || newPassword=="" )
		{
			throw new Exception("Password did not match");
			
		}
		
		return new ApiResponse(HttpStatus.OK, SUCCESS, userRepository.update(userId, user));
	}
	
	
	@CrossOrigin
	@PostMapping("/user/resetpwd")
	public ApiResponse forgotPwd( @RequestBody User user) throws Exception{
		
		String userId = user.getUserId();
		String randompwd =null;
		User loadedUser = userRepository.load(user.getUserId()); 
		if(loadedUser==null)
		{
			throw new Exception("User not found");
		}
		System.out.println("In DB userId: "+ loadedUser.getUserId()+ ", Pwd : "+ loadedUser.getPassword());
		randompwd = this.getRandomPassword(4);
		loadedUser.setPassword(randompwd);
		System.out.println("New Pwd: "+ randompwd);
		user = userRepository.update(userId, loadedUser);
		emailService.sendResetCredentailsMail(loadedUser.getEmail(), StringUtils.capitalize(user.getName()),
				loadedUser.getUserId(), loadedUser.getPassword());
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

}
