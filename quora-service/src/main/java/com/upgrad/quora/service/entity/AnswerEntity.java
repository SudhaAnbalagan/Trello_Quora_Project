package com.upgrad.quora.service.entity;

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
@Table(name = "answer", schema = "public",catalog = "quora")
@NamedQueries({
        @NamedQuery(name = "getAnswerById", query = "select q from AnswerEntity q where q.uuid = :uuid"),
        @NamedQuery(name = "editAnswerById", query = "update AnswerEntity q set q.answer = :answer where q.uuid = :uuid"),
        @NamedQuery(name = "deleteAnswerById", query = "delete from AnswerEntity q where q.uuid = :uuid "),
        @NamedQuery(name = "getAllAnswersByUser", query = "select q from AnswerEntity q where q.question.id = :id")
})
public class AnswerEntity implements Serializable {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;


  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;


  @Column(name = "ans")
  @NotNull
  @Size(max = 225)
  private String answer;

  @Column(name = "date")
  @NotNull
  private ZonedDateTime date;


  @ManyToOne
  @JoinColumn(name = "user_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private UsersEntity user;

  @ManyToOne
  @JoinColumn(name = "question_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private QuestionEntity question;


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


  public QuestionEntity getQuestion() {
    return question;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public void setQuestion(QuestionEntity question) {
    this.question = question;
  }

//  @Override
//  public int hashCode() {
//    return new HashCodeBuilder().append(this).hashCode();
//  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }


}
