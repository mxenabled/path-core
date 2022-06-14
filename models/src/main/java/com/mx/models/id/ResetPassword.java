package com.mx.models.id;

import java.util.List;

import com.mx.models.MdxBase;
import com.mx.models.challenges.Challenge;

public class ResetPassword extends MdxBase<ResetPassword> {

  //PUT /reset_password/challenges/:challenge_id will expect a single challenge
  private Challenge challenge;
  //POST /reset_password will return challenges
  private List<Challenge> challenges;

  public ResetPassword() {
  }

  public final Challenge getChallenge() {
    return challenge;
  }

  public final void setChallenge(Challenge newChallenge) {
    this.challenge = newChallenge;
  }

  public final List<Challenge> getChallenges() {
    return challenges;
  }

  public final void setChallenges(List<Challenge> newChallenges) {
    this.challenges = newChallenges;
  }
}
