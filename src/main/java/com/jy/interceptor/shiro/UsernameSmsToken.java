package com.jy.interceptor.shiro;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * Created by spring on 2017/5/25.
 */
public class UsernameSmsToken implements HostAuthenticationToken, RememberMeAuthenticationToken {
    private String username;
    private char[] code;
    private boolean rememberMe;
    private String host;

    public UsernameSmsToken() {
        this.rememberMe = false;
    }

    public UsernameSmsToken(String username, char[] code) {
        this(username, (char[])code, false, (String)null);
    }

    public UsernameSmsToken(String username, String code) {
        this(username, (char[]) (code != null ? code.toCharArray() : null), false, (String) null);
    }

    public UsernameSmsToken(String username, char[] code, String host) {
        this(username, code, false, host);
    }

    public UsernameSmsToken(String username, String code, String host) {
        this(username, code != null ? code.toCharArray() : null, false, host);
    }

    public UsernameSmsToken(String username, char[] code, boolean rememberMe) {
        this(username, (char[])code, rememberMe, (String)null);
    }

    public UsernameSmsToken(String username, String code, boolean rememberMe) {
        this(username, (char[]) (code != null ? code.toCharArray() : null), rememberMe, (String) null);
    }

    public UsernameSmsToken(String username, char[] code, boolean rememberMe, String host) {
        this.rememberMe = false;
        this.username = username;
        this.code = code;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    public UsernameSmsToken(String username, String code, boolean rememberMe, String host) {
        this(username, code != null ? code.toCharArray() : null, rememberMe, host);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getCode() {
        return this.code;
    }

    public void setCode(char[] password) {
        this.code = password;
    }

    public Object getPrincipal() {
        return this.getUsername();
    }

    public Object getCredentials() {
        return this.getCode();
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isRememberMe() {
        return this.rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void clear() {
        this.username = null;
        this.host = null;
        this.rememberMe = false;
        if(this.code != null) {
            for(int i = 0; i < this.code.length; ++i) {
                this.code[i] = 0;
            }

            this.code = null;
        }

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" - ");
        sb.append(this.username);
        sb.append(", rememberMe=").append(this.rememberMe);
        if(this.host != null) {
            sb.append(" (").append(this.host).append(")");
        }

        return sb.toString();
    }
}
