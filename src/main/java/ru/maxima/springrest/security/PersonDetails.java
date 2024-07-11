package ru.maxima.springrest.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.maxima.springrest.models.Person;

import java.util.Collection;
import java.util.Collections;

public class PersonDetails implements UserDetails {

    private final Person person;

    public PersonDetails(Person person) {
        this.person = person;
    }

    //Есть ли какуе-нибудь права у пользователя?
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
    }

    //Какой у пользователя пароль?
    @Override
    public String getPassword() {
        return person.getPassword();
    }

    //Какое у пользователя имя?
    @Override
    public String getUsername() {
        return person.getName();
    }

    //Активен ли вообще аккаунт? (не просрочен ли/бан за неактивность пользования)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Не заблокирован ли аккаунт?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Не просрочен ли логин и/или пароль?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Включена ли возможность пользоваться всеми этими spring-security-вещами?
    @Override
    public boolean isEnabled() {
        return true;
    }
}
