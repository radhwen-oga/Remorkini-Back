package com.onegateafrica.Service;

public interface PhoneverificationService {
  public VerificationResult startVerification(String phone);
  public VerificationResult checkverification(String phone, String code);
}
