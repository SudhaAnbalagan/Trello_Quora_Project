package com.upgrad.quora.service.entity;


import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;


@Entity
@Table(name = "user_auth", schema = "public",catalog = "quora")
@NamedQueries(
        @NamedQuery(name = "userByAccessToken", query = "select u from UserAuthEntity u where u.accessToken = :accessToken")
)
public class UserAuthEntity implements Serializable {


  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @NotNull
  @OnDelete(action = OnDeleteAction.CASCADE)
  private UsersEntity user;

  @Column(name = "access_token")
  @NotNull
  @Size(max = 500)
  private String accessToken;

  @Column(name = "EXPIRES_AT")
  @NotNull
  private ZonedDateTime expiresAt;

  @Column(name = "LOGIN_AT")
  @NotNull
  private ZonedDateTime loginAt;

  @Column(name = "LOGOUT_AT")
  private ZonedDateTime logoutAt;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }


  public UsersEntity getUser() {
    return user;
  }

  public void setUser(UsersEntity user) {
    this.user = user;
  }


  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }


  public ZonedDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(ZonedDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }


  public ZonedDateTime getLoginAt() {
    return loginAt;
  }

  public void setLoginAt(ZonedDateTime loginAt) {
    this.loginAt = loginAt;
  }


  public ZonedDateTime getLogoutAt() {
    return logoutAt;
  }

  public void setLogoutAt(ZonedDateTime logoutAt) {
    this.logoutAt = logoutAt;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this).hashCode();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }


}
