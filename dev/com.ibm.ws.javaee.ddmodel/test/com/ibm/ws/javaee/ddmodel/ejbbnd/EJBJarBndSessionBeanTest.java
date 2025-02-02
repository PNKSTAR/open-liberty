/*******************************************************************************
 * Copyright (c) 2012, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.javaee.ddmodel.ejbbnd;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.ibm.ws.javaee.dd.ejb.EJBJar;
import com.ibm.ws.javaee.dd.ejbbnd.EJBJarBnd;
import com.ibm.ws.javaee.dd.ejbbnd.EnterpriseBean;
import com.ibm.ws.javaee.dd.ejbbnd.Session;

@RunWith(Parameterized.class)
public class EJBJarBndSessionBeanTest extends EJBJarBndTestBase {
    @Parameters
    public static Iterable<? extends Object> data() {
        return TEST_DATA;
    }
    
    public EJBJarBndSessionBeanTest(boolean ejbInWar) {
        super(ejbInWar);
    }

    //

    protected static final String sessionXML1 =
            "<session name=\"SessionBean1\">\n" +
            "</session>";

    @Test
    public void testSessionEmptyLists() throws Exception {
        EJBJarBnd ejbJarBnd = parseEJBJarBndXML(ejbJarBnd10(sessionXML1));

        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Session session0 = (Session) sessionBeans.get(0);
        Assert.assertEquals(0, session0.getEJBRefs().size());
        Assert.assertEquals(0, session0.getInterfaces().size());
        Assert.assertEquals(0, session0.getMessageDestinationRefs().size());
        Assert.assertEquals(0, session0.getResourceEnvRefs().size());
        Assert.assertEquals(0, session0.getResourceRefs().size());
    }

    @Test
    public void testSessionEmptyXMI() throws Exception {
        EJBJar ejbJar = parseEJBJar(
                ejbJar21(
                    "<enterprise-beans>" +
                        "<session id=\"s0\"/>" +
                    "</enterprise-beans>"));

        EJBJarBnd ejbJarBnd = parseEJBJarBndXMI(
                ejbJarBndXMI("",
                    "<ejbBindings>" +
                        "<enterpriseBean xmi:type=\"ejb:Session\" href=\"" + getEJBJarPath() + "#s0\"/>" +
                    "</ejbBindings>"),
                ejbJar);
        
        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Session session0 = (Session) sessionBeans.get(0);
        Assert.assertNull(session0.getName());
        Assert.assertEquals(0, session0.getEJBRefs().size());
        Assert.assertEquals(0, session0.getInterfaces().size());
        Assert.assertEquals(0, session0.getMessageDestinationRefs().size());
        Assert.assertEquals(0, session0.getResourceEnvRefs().size());
        Assert.assertEquals(0, session0.getResourceRefs().size());
    }

    @Test
    public void testSessionAttributeNameTest() throws Exception {
        EJBJarBnd ejbJarBnd = parseEJBJarBndXML(ejbJarBnd11(sessionXML1));

        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Session session0 = (Session) sessionBeans.get(0);
        Assert.assertEquals("SessionBean1", session0.getName());
    }

    @Test
    public void testSessionAttributeNameTestXMI() throws Exception {
        EJBJar ejbJar = parseEJBJar(
                ejbJar21(
                    "<enterprise-beans>" +
                        "<session id=\"s0\">" +
                            "<ejb-name>SessionBean1</ejb-name>" +
                        "</session>" +
                    "</enterprise-beans>"));
                
        EJBJarBnd ejbJarBnd = parseEJBJarBndXMI(
                ejbJarBndXMI("",
                    "<ejbBindings>" +
                        "<enterpriseBean xmi:type=\"ejb:Session\" href=\"" + getEJBJarPath() + "#s0\"/>" +
                    "</ejbBindings>"),
                ejbJar);
        
        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Session session0 = (Session) sessionBeans.get(0);
        Assert.assertEquals("SessionBean1", session0.getName());
    }

    protected static final String sessionXML2 =
            "<session name=\"SessionBean2\" simple-binding-name=\"SimpleBindingName2\"/>";

    @Test
    public void testSessionAttributeSimpleBindingTest() throws Exception {
        EJBJarBnd ejbJarBnd = parseEJBJarBndXML(ejbJarBnd11(sessionXML2));
        
        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Session session0 = (Session) sessionBeans.get(0);
        Assert.assertEquals("SessionBean2", session0.getName());
        Assert.assertEquals("SimpleBindingName2", session0.getSimpleBindingName());
    }

    @Test
    public void testSessionAttributeSimpleBindingTestXMI() throws Exception {
        EJBJar ejbJar = parseEJBJar(
                ejbJar21(
                    "<enterprise-beans>" +
                        "<session id=\"s0\">" +
                            "<ejb-name>SessionBean2</ejb-name>" +
                        "</session>" +
                    "</enterprise-beans>"));
                
        EJBJarBnd ejbJarBnd = parseEJBJarBndXMI(
                ejbJarBndXMI("",
                    "<ejbBindings jndiName=\"SimpleBindingName2\">" +
                        "<enterpriseBean xmi:type=\"ejb:Session\" href=\"" + getEJBJarPath() + "#s0\"/>" +
                    "</ejbBindings>"),
                ejbJar);

        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Session session0 = (Session) sessionBeans.get(0);
        Assert.assertEquals("SessionBean2", session0.getName());
        Assert.assertEquals("SimpleBindingName2", session0.getSimpleBindingName());
    }

    protected static final String sessionXML3 =
            "<session name=\"SessionBean3\" component-id=\"ComponentId3\"/>";

    @Test
    public void testSessionAttributeComponentIdTest() throws Exception {
        EJBJarBnd ejbJarBnd = parseEJBJarBndXML(ejbJarBnd11(sessionXML3));

        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Session session0 = (Session) sessionBeans.get(0);
        Assert.assertEquals("SessionBean3", session0.getName());
        Assert.assertEquals("ComponentId3", session0.getComponentID());
    }

    protected static final String sessionXML4 =
            "<session name=\"SessionBean4\" remote-home-binding-name=\"remoteHomeBindingName4\"/>";

    @Test
    public void testSessionAttributeRemoteHomeBindingTest() throws Exception {
        EJBJarBnd ejbJarBnd = parseEJBJarBndXML(ejbJarBnd11(sessionXML4));

        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Session session0 = (Session) sessionBeans.get(0);
        Assert.assertEquals("SessionBean4", session0.getName());
        Assert.assertEquals("remoteHomeBindingName4", session0.getRemoteHomeBindingName());
    }

    protected static final String sessionXML5 =
            "<session name=\"SessionBean5\" local-home-binding-name=\"localHomeBindingName5\"/>";

    @Test
    public void testSessionAttributeLocalHomeBindingTest() throws Exception {
        EJBJarBnd ejbJarBnd = parseEJBJarBndXML(ejbJarBnd11(sessionXML5));

        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Session session0 = (Session) sessionBeans.get(0);
        Assert.assertEquals("SessionBean5", session0.getName());
        Assert.assertEquals("localHomeBindingName5", session0.getLocalHomeBindingName());
    }

    protected static final String sessionXML6 =
            "<session name=\"SessionBean6\">\n" +
                "<interface class=\"SBInterface6\" binding-name=\"SBInterfaceBinding6\"/>\n" +
            "</session>";

    @Test
    public void testSessionElementInterfaceTest() throws Exception {
        EJBJarBnd ejbJarBnd = parseEJBJarBndXML(ejbJarBnd11(sessionXML6));

        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Session session0 = (Session) sessionBeans.get(0);
        Assert.assertEquals("SessionBean6", session0.getName());
        Assert.assertEquals("SBInterface6", session0.getInterfaces().get(0).getClassName());
        Assert.assertEquals("SBInterfaceBinding6", session0.getInterfaces().get(0).getBindingName());
    }

    protected static final String sessionXML7 =
            "<session name=\"SessionBean7\">\n" +
                "<ejb-ref name=\"SBEjbRefName7\"\n" +
                "binding-name=\"SBBindingName7\"/>\n" +
            "</session>";

    @Test
    public void testSessionElementEJBRefTest() throws Exception {
        EJBJarBnd ejbJarBnd = parseEJBJarBndXML(ejbJarBnd11(sessionXML7));

        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Assert.assertEquals("SessionBean7", ((Session) sessionBeans.get(0)).getName());
        Assert.assertEquals("SBEjbRefName7", sessionBeans.get(0).getEJBRefs().get(0).getName());
        Assert.assertEquals("SBBindingName7", sessionBeans.get(0).getEJBRefs().get(0).getBindingName());
    }

    @Test
    public void testSessionElementEJBRefXMI() throws Exception {
        EJBJar ejbJar = parseEJBJar(
                ejbJar21(
                    "<enterprise-beans>" +
                        "<session id=\"s0\">" +
                            "<ejb-name>SessionBean7</ejb-name>" +
                            "<ejb-ref id=\"er0\">" +
                                "<ejb-ref-name>SBEjbRefName7</ejb-ref-name>" +
                            "</ejb-ref>" +
                        "</session>" +
                    "</enterprise-beans>"));

        EJBJarBnd ejbJarBnd = parseEJBJarBndXMI(
                ejbJarBndXMI("",
                    "<ejbBindings>" +
                        "<enterpriseBean xmi:type=\"ejb:Session\" href=\"" + getEJBJarPath() + "#s0\"/>" +
                        "<ejbRefBindings jndiName=\"SBBindingName7\">" +
                            "<bindingEjbRef href=\"" + getEJBJarPath() + "#er0\"/>" +
                        "</ejbRefBindings>" +
                    "</ejbBindings>"),
                ejbJar);
        
        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Assert.assertEquals("SessionBean7", ((Session) sessionBeans.get(0)).getName());
        Assert.assertEquals("SBEjbRefName7", sessionBeans.get(0).getEJBRefs().get(0).getName());
        Assert.assertEquals("SBBindingName7", sessionBeans.get(0).getEJBRefs().get(0).getBindingName());
    }

    @Test
    public void testSessionElementResourceRefTest() throws Exception {
        EJBJarBnd ejbJarBnd = parseEJBJarBndXML(ejbJarBnd11(sessionXML8));

        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Assert.assertEquals("SessionBean8", sessionBeans.get(0).getName());
        Assert.assertEquals("resourceRefName8", sessionBeans.get(0).getResourceRefs().get(0).getName());
        Assert.assertEquals("resourceRefBinding8", sessionBeans.get(0).getResourceRefs().get(0).getBindingName());
    }

    @Test
    public void testSessionElementResourceRefXMI() throws Exception {
        EJBJar ejbJar = parseEJBJar(
                ejbJar21(
                    "<enterprise-beans>" +
                        "<session id=\"s0\">" +
                            "<ejb-name>SessionBean8</ejb-name>" +
                            "<resource-ref id=\"rr0\">" +
                                "<res-ref-name>resourceRefName8</res-ref-name>" +
                            "</resource-ref>" +
                        "</session>" +
                    "</enterprise-beans>"));

        EJBJarBnd ejbJarBnd = parseEJBJarBndXMI(
                ejbJarBndXMI("",
                    "<ejbBindings>" +
                        "<enterpriseBean xmi:type=\"ejb:Session\" href=\"" + getEJBJarPath() + "#s0\"/>" +
                        "<resRefBindings jndiName=\"resourceRefBinding8\">" +
                            "<bindingResourceRef href=\"" + getEJBJarPath() + "#rr0\"/>" +
                        "</resRefBindings>" +
                    "</ejbBindings>"),
                ejbJar);
        
        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Assert.assertEquals("SessionBean8", sessionBeans.get(0).getName());
        Assert.assertEquals("resourceRefName8", sessionBeans.get(0).getResourceRefs().get(0).getName());
        Assert.assertEquals("resourceRefBinding8", sessionBeans.get(0).getResourceRefs().get(0).getBindingName());
    }

    /*
     * protected static final String sessionXML11 =
     * "<session name=\"SessionBean11\"> " +
     * "<resource-ref name=\"ResourceRef11\" binding-name=\"ResourceRefBindingName11\"> " +
     * "<authentication-alias name=\"AuthAlias11\" />" +
     * "<custom-login-configuration name=\"customLoginConfiguration11\"> " +
     * "<property name=\"propname\" value=\"propvalue\"/> " +
     * "</custom-login-configuration> " +
     * "</resource-ref>" +
     * "</session>";
     */

    @Test
    public void testSessionElementResourceRefOptionalElements() throws Exception {
        EJBJarBnd ejbJarBnd = parseEJBJarBndXML(ejbJarBnd11(sessionXML11));        

        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Session session = (Session) sessionBeans.get(0);
        Assert.assertEquals("SessionBean11", session.getName());

        Assert.assertEquals(1, session.getResourceRefs().size());
        Assert.assertEquals("ResourceRef11", session.getResourceRefs().get(0).getName());
        Assert.assertEquals("ResourceRefBindingName11", session.getResourceRefs().get(0).getBindingName());
        Assert.assertEquals("AuthAlias11", session.getResourceRefs().get(0).getAuthenticationAlias().getName());
        Assert.assertEquals("customLoginConfiguration11", session.getResourceRefs().get(0).getCustomLoginConfiguration().getName());
        Assert.assertEquals("propname", session.getResourceRefs().get(0).getCustomLoginConfiguration().getProperties().get(0).getName());
        Assert.assertEquals("propvalue", session.getResourceRefs().get(0).getCustomLoginConfiguration().getProperties().get(0).getValue());
    }

    @Test
    public void testSessionElementResourceRefOptionalElementsXMI() throws Exception {
        EJBJar ejbJar = parseEJBJar(
                ejbJar21(
                    "<enterprise-beans>" +
                        "<session id=\"s0\">" +
                            "<ejb-name>SessionBean11</ejb-name>" +
                            "<resource-ref id=\"rr0\">" +
                                "<res-ref-name>ResourceRef11</res-ref-name>" +
                            "</resource-ref>" +
                        "</session>" +
                    "</enterprise-beans>"));
                
        EJBJarBnd ejbJarBnd = parseEJBJarBndXMI(
                ejbJarBndXMI("",
                    "<ejbBindings>" +
                        "<enterpriseBean xmi:type=\"ejb:Session\" href=\"" + getEJBJarPath() + "#s0\"/>" +
                        "<resRefBindings jndiName=\"ResourceRefBindingName11\" loginConfigurationName=\"customLoginConfiguration11\">" +
                            "<bindingResourceRef href=\"" + getEJBJarPath() + "#rr0\"/>" +
                            "<properties name=\"propname\" value=\"propvalue\"/>" +
                        "</resRefBindings>" +
                    "</ejbBindings>"),
                ejbJar);
        
        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Session session = (Session) sessionBeans.get(0);
        Assert.assertEquals("SessionBean11", session.getName());

        Assert.assertEquals(1, session.getResourceRefs().size());
        Assert.assertEquals("ResourceRef11", session.getResourceRefs().get(0).getName());
        Assert.assertEquals("ResourceRefBindingName11", session.getResourceRefs().get(0).getBindingName());
        Assert.assertNull(session.getResourceRefs().get(0).getAuthenticationAlias());
        Assert.assertEquals("customLoginConfiguration11", session.getResourceRefs().get(0).getCustomLoginConfiguration().getName());
        Assert.assertEquals("propname", session.getResourceRefs().get(0).getCustomLoginConfiguration().getProperties().get(0).getName());
        Assert.assertEquals("propvalue", session.getResourceRefs().get(0).getCustomLoginConfiguration().getProperties().get(0).getValue());
    }

    protected static final String sessionXML9 =
            "<session name=\"SessionBean9\">" +
                "<resource-env-ref name=\"resourceEnvRef9\" binding-name=\"resourceEnvRefBindingName9\"/>" +
            "</session>";

    @Test
    public void testSessionElementResourceEnvRefTest() throws Exception {
        EJBJarBnd ejbJarBnd = parseEJBJarBndXML(ejbJarBnd10(sessionXML9));

        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Assert.assertEquals(1, sessionBeans.get(0).getResourceEnvRefs().size());
        Assert.assertEquals("SessionBean9", sessionBeans.get(0).getName());
        Assert.assertEquals("resourceEnvRef9", sessionBeans.get(0).getResourceEnvRefs().get(0).getName());
        Assert.assertEquals("resourceEnvRefBindingName9", sessionBeans.get(0).getResourceEnvRefs().get(0).getBindingName());
    }

    @Test
    public void testSessionElementResourceEnvRefTestXMI() throws Exception {
        EJBJar ejbJar = parseEJBJar(
                ejbJar21(
                    "<enterprise-beans>" +
                        "<session id=\"s0\">" +
                            "<ejb-name>SessionBean9</ejb-name>" +
                            "<resource-env-ref id=\"rer0\">" +
                                "<resource-env-ref-name>resourceEnvRef9</resource-env-ref-name>" +
                            "</resource-env-ref>" +
                        "</session>" +
                    "</enterprise-beans>"));
                
        EJBJarBnd ejbJarBnd = parseEJBJarBndXMI(
                ejbJarBndXMI("",
                    "<ejbBindings>" +
                        "<enterpriseBean xmi:type=\"ejb:Session\" href=\"" + getEJBJarPath() + "#s0\"/>" +
                        "<resourceEnvRefBindings jndiName=\"resourceEnvRefBindingName9\">" +
                            "<bindingResourceEnvRef href=\"" + getEJBJarPath() + "#rer0\"/>" +
                        "</resourceEnvRefBindings>" +
                    "</ejbBindings>"),
                ejbJar);
        
        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Assert.assertEquals(1, sessionBeans.get(0).getResourceEnvRefs().size());
        Assert.assertEquals("SessionBean9", sessionBeans.get(0).getName());
        Assert.assertEquals("resourceEnvRef9", sessionBeans.get(0).getResourceEnvRefs().get(0).getName());
        Assert.assertEquals("resourceEnvRefBindingName9", sessionBeans.get(0).getResourceEnvRefs().get(0).getBindingName());
    }

    protected static final String sessionXML10 =
            "<session name=\"SessionBean10\">" +
                "<message-destination-ref name=\"messageDestinationName10\" binding-name=\"messageDestinationBindingName10\"/>" +
            "</session>";

    @Test
    public void testSessionElementMessageDestinationTest() throws Exception {
        EJBJarBnd ejbJarBnd = parseEJBJarBndXML(ejbJarBnd10(sessionXML10));

        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Assert.assertEquals(1, sessionBeans.get(0).getMessageDestinationRefs().size());
        Assert.assertEquals("SessionBean10", sessionBeans.get(0).getName());
        Assert.assertEquals("messageDestinationName10", sessionBeans.get(0).getMessageDestinationRefs().get(0).getName());
        Assert.assertEquals("messageDestinationBindingName10", sessionBeans.get(0).getMessageDestinationRefs().get(0).getBindingName());

    }

    @Test
    public void testSessionElementMessageDestinationXMI() throws Exception {
        EJBJar ejbJar = parseEJBJar(
                ejbJar21(
                    "<enterprise-beans>" +
                        "<session id=\"s0\">" +
                            "<ejb-name>SessionBean10</ejb-name>" +
                            "<message-destination-ref id=\"mdr0\">" +
                                "<message-destination-ref-name>messageDestinationName10</message-destination-ref-name>" +
                            "</message-destination-ref>" +
                        "</session>" +
                    "</enterprise-beans>"));

        EJBJarBnd ejbJarBnd = parseEJBJarBndXMI(
                ejbJarBndXMI("",
                    "<ejbBindings>" +
                        "<enterpriseBean xmi:type=\"ejb:Session\" href=\"" + getEJBJarPath() + "#s0\"/>" +
                        "<messageDestinationRefBindings jndiName=\"messageDestinationBindingName10\">" +
                            "<bindingMessageDestinationRef href=\"" + getEJBJarPath() + "#mdr0\"/>" +
                        "</messageDestinationRefBindings>" +
                    "</ejbBindings>"),
                ejbJar);
        
        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(1, sessionBeans.size());
        Assert.assertEquals(1, sessionBeans.get(0).getMessageDestinationRefs().size());
        Assert.assertEquals("SessionBean10", sessionBeans.get(0).getName());
        Assert.assertEquals("messageDestinationName10", sessionBeans.get(0).getMessageDestinationRefs().get(0).getName());
        Assert.assertEquals("messageDestinationBindingName10", sessionBeans.get(0).getMessageDestinationRefs().get(0).getBindingName());

    }

    @Test
    public void testSessionAllBeans() throws Exception {
        EJBJarBnd ejbJarBnd = parseEJBJarBndXML(
                ejbJarBnd11(
                    sessionXML1 +
                    sessionXML2 +
                    sessionXML3 +
                    sessionXML4 +
                    sessionXML5 +
                    sessionXML6 +
                    sessionXML7 +
                    sessionXML8 +
                    sessionXML9 +
                    sessionXML10 +
                    sessionXML11));

        List<EnterpriseBean> sessionBeans = ejbJarBnd.getEnterpriseBeans();
        Assert.assertEquals(11, sessionBeans.size());
    }
}
