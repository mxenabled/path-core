package com.mx.models.id;

import java.util.List;

import com.mx.models.MdxBase;
import com.mx.models.challenges.Challenge;

public class ForgotUsername extends MdxBase<ForgotUsername> {

  //PUT /forgot_username/challenges/:challenge_id will expect a single challenge
  private Challenge challenge;
  //POST /forgot_username will return challenges
  private List<Challenge> challenges;

  public ForgotUsername() {
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
