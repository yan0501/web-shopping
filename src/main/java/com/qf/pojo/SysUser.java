package com.qf.pojo;

public class SysUser {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_user.id
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_user.tel
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    private String tel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_user.password
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    private String password;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_user.email
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    private String email;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_user.invitation
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    private String invitation;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_user.id
     *
     * @return the value of sys_user.id
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_user.id
     *
     * @param id the value for sys_user.id
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_user.tel
     *
     * @return the value of sys_user.tel
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    public String getTel() {
        return tel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_user.tel
     *
     * @param tel the value for sys_user.tel
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_user.password
     *
     * @return the value of sys_user.password
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_user.password
     *
     * @param password the value for sys_user.password
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_user.email
     *
     * @return the value of sys_user.email
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_user.email
     *
     * @param email the value for sys_user.email
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_user.invitation
     *
     * @return the value of sys_user.invitation
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    public String getInvitation() {
        return invitation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_user.invitation
     *
     * @param invitation the value for sys_user.invitation
     *
     * @mbg.generated Mon Jul 13 14:54:17 CST 2020
     */
    public void setInvitation(String invitation) {
        this.invitation = invitation == null ? null : invitation.trim();
    }
}