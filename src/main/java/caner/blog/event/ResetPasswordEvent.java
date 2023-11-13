package caner.blog.event;

import caner.blog.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ResetPasswordEvent extends ApplicationEvent {

    private User user;
    private String confirmationUrl;

    public ResetPasswordEvent(User user, String confirmationUrl) {
        super(user);
        this.user = user;
        this.confirmationUrl = confirmationUrl;
    }
}
