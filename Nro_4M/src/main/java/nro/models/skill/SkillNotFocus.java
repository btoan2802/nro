package nro.models.skill;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Hoàng Việt - 0857853150
 */
@Setter
@Getter
public class SkillNotFocus extends Skill {

    private int timePre;

    private int timeDame;

    private short range;

    public SkillNotFocus(Skill skill) {
        super(skill);
        this.timePre = 2000;
        this.timeDame = 5000;
        this.range = 250;
    }
}
