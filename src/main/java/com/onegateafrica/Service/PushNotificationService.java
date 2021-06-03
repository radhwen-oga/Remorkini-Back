package com.onegateafrica.Service;

import io.github.jav.exposerversdk.PushClientException;

public interface PushNotificationService {
	void ajouterPushNotification() throws PushClientException, InterruptedException;
}
