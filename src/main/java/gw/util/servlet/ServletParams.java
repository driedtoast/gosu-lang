package gw.util.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ServletParams {
  private HttpServletRequest _request;
  private HttpServletResponse _response;
  private HttpSession _session;
  private ServletContext _ctx;

  public ServletParams( HttpServletRequest request, HttpServletResponse response, HttpSession session, ServletContext ctx ) {
    _request = request;
    _response = response;
    _session = session;
    _ctx = ctx;
  }

  public HttpServletRequest getRequest() {
    return _request;
  }

  public HttpServletResponse getResponse() {
    return _response;
  }

  public HttpSession getSession() {
    return _session;
  }

  public ServletContext getServletContext() {
    return _ctx;
  }
  
}
