package net.sweenus.simplyskills.rewards;

import net.minecraft.util.Identifier;
import net.puffish.skillsmod.SkillsMod;
import net.puffish.skillsmod.api.SkillsAPI;
import net.puffish.skillsmod.api.json.JsonElement;
import net.puffish.skillsmod.api.json.JsonObject;
import net.puffish.skillsmod.api.reward.Reward;
import net.puffish.skillsmod.api.reward.RewardConfigContext;
import net.puffish.skillsmod.api.reward.RewardDisposeContext;
import net.puffish.skillsmod.api.reward.RewardUpdateContext;
import net.puffish.skillsmod.api.util.Problem;
import net.puffish.skillsmod.api.util.Result;

import java.util.ArrayList;

public class PassiveSkillReward implements Reward {
    public static final Identifier ID = SkillsMod.createIdentifier("passive_skill");

    private final String passiveSkillId;

	private PassiveSkillReward(String passiveSkillId) {
		this.passiveSkillId = passiveSkillId;
	}

	public static void register() {
        SkillsAPI.registerReward(ID, PassiveSkillReward::create);
    }

    private static Result<PassiveSkillReward, Problem> create(RewardConfigContext context) {
        return context.getData()
                .andThen(JsonElement::getAsObject)
                .andThen(PassiveSkillReward::create);
    }

    private static Result<PassiveSkillReward, Problem> create(JsonObject rootObject) {
        var problems = new ArrayList<Problem>();

        var optPassiveSkillId = rootObject.get("passive_skill")
                .andThen(JsonElement::getAsString)
                .ifFailure(problems::add)
                .getSuccess();

        if (problems.isEmpty()) {
            return Result.success(new PassiveSkillReward(
                    optPassiveSkillId.orElseThrow()
            ));
        } else {
            return Result.failure(Problem.combine(problems));
        }
    }

    @Override
    public void update(RewardUpdateContext rewardUpdateContext) {
        // nothing to do
    }

    @Override
    public void dispose(RewardDisposeContext rewardDisposeContext) {
        // nothing to do
    }
}
