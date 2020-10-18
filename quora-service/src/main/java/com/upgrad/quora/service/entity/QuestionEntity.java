package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "question", schema = "public",catalog = "quora")
@NamedQueries({
        @NamedQuery(name = "getAllQuestions", query = "select q from QuestionEntity q"),
        @NamedQuery(name = "getQuestionById", query = "select q from QuestionEntity q where q.uuid = :uuid"),
        @NamedQuery(name = "editQuestionById", query = "update QuestionEntity q set q.content = :content where q.uuid = :uuid"),
        @NamedQuery(name = "deleteQuestionById", query = "delete from QuestionEntity q where q.uuid = :uuid "),
        @NamedQuery(name = "getAllQuestionsByUser", query = "select q from QuestionEntity q where q.user = :user")
}

)
public class QuestionEntity implements Serializable {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;


  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;


  @Column(name = "content")
  @NotNull
  @Size(max = 500)
  private String content;


  @Column(name = "date")
  @NotNull
  private ZonedDateTime date;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private UsersEntity user;

  @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
  private List<AnswerEntity> answer;

  //@Override
 /* public int hashCode() {
    return new HashCodeBuilder().append(this).hashCode();
  }*/

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }

  @Override
  public boolean equals(Object obj) {
    return new EqualsBuilder().append(this, obj).isEquals();
  }

  public List<AnswerEntity> getAnswer() {
    return answer;
  }

  public void setAnswer(List<AnswerEntity> answer) {
    this.answer = answer;
  }


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


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public ZonedDateTime getDate() {
    return date;
  }

  public void setDate(ZonedDateTime date) {
    this.date = date;
  }


  public UsersEntity getUser() {
    return user;
  }

  public void setUser(UsersEntity user) {
    this.user = user;
  }



}
