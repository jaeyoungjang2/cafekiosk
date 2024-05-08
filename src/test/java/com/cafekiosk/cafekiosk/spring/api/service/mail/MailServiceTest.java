package com.cafekiosk.cafekiosk.spring.api.service.mail;

import com.cafekiosk.cafekiosk.spring.client.mail.MailSendClient;
import com.cafekiosk.cafekiosk.spring.domain.history.mail.MailSendHistory;
import com.cafekiosk.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

//    @Spy
    @Mock
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
//        @Mock private MailSendClient mailSendClient 로 대체됨
//        MailSendClient mailSendClient = Mockito.mock(MailSendClient.class);

//        @Mock private MailSendHistoryRepository mailSendHistoryRepository 로 대체됨
//        MailSendHistoryRepository mailSendHistoryRepository = Mockito.mock(MailSendHistoryRepository.class);

//        @InjectMocks private MailService mailService 으로 대체됨
//        MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);


        // given
//        Mockito.when(mailSendClient.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
//            .thenReturn(true);

        BDDMockito.given(mailSendClient.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .willReturn(true);

//        mailSendClient에 Mock 어노테이션 적용하는 대신 @Spy 어노테이션 적용하는 경우 아래와 같이 사용할 수 있다.
//        Mockito.doReturn(true)
//                .when(mailSendClient)
//                .sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        // when
        boolean result = mailService.sendMail("", "", "", "");

        // then
        assertThat(result).isTrue();
        Mockito.verify(mailSendHistoryRepository, Mockito.times(1)).save(Mockito.any(MailSendHistory.class));
    }

}