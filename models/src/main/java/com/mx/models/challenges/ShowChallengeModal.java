package com.mx.models.challenges;

import com.mx.models.MdxBase;

public final class ShowChallengeModal extends MdxBase<ShowChallengeModal> {
  private Challenge challenge;

  public Challenge getChallenge() {
    return challenge;
  }

  public void setChallenge(Challenge challenge) {
    this.challenge = challenge;
  }
}
