package 开闭原则;

import java.util.List;

/**
 * create by lwj on 2019/12/13
 */
public abstract class Hero {
    private List<Skill> skills;

    Hero(List<Skill>  skills) {
        this.skills = skills;
    }

    final void execSkill(Integer skillNumber) {
        skills.get(skillNumber).attack();
    }

}
