package com.toy.everyday;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;



@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateForm {

    @NotEmpty(message = "이메일은 필수항목입니다.")
    private String email;

    @NotEmpty(message = "이름은 필수항목입니다.")
    private String name;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;






}
