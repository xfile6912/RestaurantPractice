package kr.co.fastcampus.eatgo.domain;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private Long restaurantId;

    @NotEmpty
    @Setter
    private String email;

    @NotEmpty
    @Setter
    private String name;

    @NotNull
    @Setter
    private Long level;


    private String password;

    public boolean isAdmin(){
        if(level>=100)
            return true;
        return false;
    }
    public boolean isActive(){
        return level>0;
    }

    public void deactivate() {
        level=0L;
    }

    public void setRestaurantId(Long restaurantId){
        level=50L;
        this.restaurantId=restaurantId;
    }

    public boolean isRestaurantOwner() {
        return  level==50L;
    }
}
