package 开闭原则;

import java.util.ArrayList;
import java.util.List;

/**
 * create by lwj on 2019/12/13
 */
public class Run {
    public static void main(String[] args) {
        List<Skill> skills=new ArrayList<>();
        SkillOne skillOne = new SkillOne();
        SkillTwo skillTwo = new SkillTwo();
        skills.add(skillOne);
        skills.add(skillTwo);

        LuBanQiHao luBanQiHao = new LuBanQiHao(skills);
        luBanQiHao.execSkill(0);
        luBanQiHao.execSkill(1);

    }
}
