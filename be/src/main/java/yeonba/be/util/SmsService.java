package yeonba.be.util;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService {

	@Value("${SMS_SENDER}")
	private String sender;
	private final DefaultMessageService messageService;

	public void sendMessage(String to, String text) {
		Message message = new Message();
		message.setFrom(sender);
		message.setTo(to);
		message.setText(text);

		messageService.sendOne(new SingleMessageSendingRequest(message));
	}
}
