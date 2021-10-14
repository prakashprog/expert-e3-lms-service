package com.expertworks.lms.util;

import java.net.MalformedURLException;
import java.net.URL;

import freemarker.cache.URLTemplateLoader;

public class CloudTemplateLoader extends URLTemplateLoader {
	
	private URL root;
	public CloudTemplateLoader(URL root) {
	    super();
	    this.root = root;
	}

	@Override
	protected URL getURL(String template) {
	    try {
	        return new URL(root, "/" + template);
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

}
