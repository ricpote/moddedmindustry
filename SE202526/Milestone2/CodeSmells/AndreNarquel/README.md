## Code Smells

# Change Log
- 7/11/2025 André Narquel

# Long Method
Location: build/src/mindustry/entities/abilities/EnergyFieldAbility.java

The update(Unit unit) method shows a Long Method code smell because it performs too many tasks within a single 
block of code. It updates visuals, manages timing and ammunition, searches for nearby units, applies healing or damage,
and triggers effects. This makes the method difficult to read, maintain, and test, as it violates the single 
responsibility principle.

Solution: Split the method into smaller ones, each handling one specific task such as updating
visuals, checking activation, collecting targets, and processing effects.

//Code snippet

    public void update(Unit unit){

        curStroke = Mathf.lerpDelta(curStroke, anyNearby ? 1 : 0, 0.09f);

        if((timer += Time.delta) >= reload && (!useAmmo || unit.ammo > 0 || !state.rules.unitAmmo)){
            Tmp.v1.trns(unit.rotation - 90, x, y).add(unit.x, unit.y);
            float rx = Tmp.v1.x, ry = Tmp.v1.y;
            anyNearby = false;

            all.clear();

            if(hitUnits){
                Units.nearby(null, rx, ry, range, other -> {
                    if(other != unit && other.checkTarget(targetAir, targetGround) && other.targetable(unit.team) && (other.team != unit.team || other.damaged())){
                        all.add(other);
                    }
                });
            }

            if(hitBuildings && targetGround){
                Units.nearbyBuildings(rx, ry, range, b -> {
                    if((b.team != Team.derelict || state.rules.coreCapture) && ((b.team != unit.team && b.block.targetable) || b.damaged()) && !b.block.privileged){
                        all.add(b);
                    }
                });
            }

            all.sort(h -> h.dst2(rx, ry));
            int len = Math.min(all.size, maxTargets);
            for(int i = 0; i < len; i++){
                Healthc other = all.get(i);

                //lightning gets absorbed by plastanium
                var absorber = Damage.findAbsorber(unit.team, rx, ry, other.getX(), other.getY());
                if(absorber != null){
                    other = absorber;
                }

                if(((Teamc)other).team() == unit.team){
                    if(other.damaged()){
                        anyNearby = true;
                        float healMult = (other instanceof Unit u && u.type == unit.type) ? sameTypeHealMult : 1f;
                        other.heal(healPercent / 100f * other.maxHealth() * healMult);
                        healEffect.at(other);
                        damageEffect.at(rx, ry, 0f, color, other);
                        hitEffect.at(rx, ry, unit.angleTo(other), color);

                        if(other instanceof Building b){
                            Fx.healBlockFull.at(b.x, b.y, 0f, color, b.block);
                        }
                    }
                }else{
                    anyNearby = true;
                    if(other instanceof Building b){
                        b.damage(unit.team, damage * state.rules.unitDamage(unit.team));
                    }else{
                        other.damage(damage * state.rules.unitDamage(unit.team));
                    }
                    if(other instanceof Statusc s){
                        s.apply(status, statusDuration);
                    }
                    hitEffect.at(other.x(), other.y(), unit.angleTo(other), color);
                    damageEffect.at(rx, ry, 0f, color, other);
                    hitEffect.at(rx, ry, unit.angleTo(other), color);
                }
            }

            if(anyNearby){
                shootSound.at(unit);

                if(useAmmo && state.rules.unitAmmo){
                    unit.ammo --;
                }
            }

            timer = 0f;
        }
    }


# Large Class
Location: core/src/mindustry/service/GameService.java

The GameService class shows a Large Class code smell because it handles too many responsibilities within a single
class. It manages achievements, game events, campaign progress, statistics, and data storage. This makes the class 
difficult to understand, modify, and test.

Solution: An AchievementService could handle all achievement logic, a CampaignService could manage 
campaign-related progress, an EventHandlerService could register and process events, and a StatsService could manage 
statistics and data persistence.

//Code snippet

    private void registerEvents(){
        (...)
    }
   
    private void checkUpdate(){
        (...)
    }

    private void save(){
       (...)
    }

    private void trigger(Trigger trigger, Achievement ach){
       (...)
    }

    private boolean campaign(){
        (...)
    }


# Message Chain
Location: core/src/mindustry/net/BeControl.java

This line of code shows a Message Chain code smell because it performs a long sequence of method calls to retrieve a 
deeply nested value. It depends on the internal structure of multiple classes, making the code hard to read, and tightly
coupled. If any intermediate method changes or fails, the whole chain can break, leading to confusing bug fixing.

Solution: Instead of chaining multiple calls, store intermediate results in clearly named variables.

//Code snippet
    
     public void showUpdateDialog(){
        (...)
        Fi.get(BeControl.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        (...)
     }
    