package com.spring.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Created by Administrator on 2016/11/11.
 */
public class MyRealm extends AuthorizingRealm {
    private String name = "1";
    private String password = "1";

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // ���ȷ��ҳ����<shiro:hasRole>��ǩ��name��ֵ
        info.addRole("admin");
        // �������ҳ���� <shiro:hasPermission> ��ǩ��name��ֵ
        info.addStringPermission("user:edit");
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToke = (UsernamePasswordToken) token;
        String name = usernamePasswordToke.getUsername();
        if (!this.name.equals(name)) {
            throw new UnknownAccountException();// �ʺŲ�����
        }
        return new SimpleAuthenticationInfo(this.name, this.password, getName());
    }
}
