package mindustry.game;

import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.struct.Seq;
import arc.util.Structs;
import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

import static mindustry.content.UnitTypes.*;
import static mindustry.content.UnitTypes.aegires;
import static mindustry.content.UnitTypes.antumbra;
import static mindustry.content.UnitTypes.arkyid;
import static mindustry.content.UnitTypes.atrax;
import static mindustry.content.UnitTypes.bryde;
import static mindustry.content.UnitTypes.corvus;
import static mindustry.content.UnitTypes.crawler;
import static mindustry.content.UnitTypes.cyerce;
import static mindustry.content.UnitTypes.eclipse;
import static mindustry.content.UnitTypes.flare;
import static mindustry.content.UnitTypes.horizon;
import static mindustry.content.UnitTypes.minke;
import static mindustry.content.UnitTypes.navanax;
import static mindustry.content.UnitTypes.nova;
import static mindustry.content.UnitTypes.omura;
import static mindustry.content.UnitTypes.oxynoe;
import static mindustry.content.UnitTypes.pulsar;
import static mindustry.content.UnitTypes.quad;
import static mindustry.content.UnitTypes.quasar;
import static mindustry.content.UnitTypes.reign;
import static mindustry.content.UnitTypes.risso;
import static mindustry.content.UnitTypes.scepter;
import static mindustry.content.UnitTypes.sei;
import static mindustry.content.UnitTypes.spiroct;
import static mindustry.content.UnitTypes.toxopid;
import static mindustry.content.UnitTypes.vela;
import static mindustry.content.UnitTypes.zenith;

public class InfiniteWaveClass {
    private Rand rand;
    private float baseDifficulty;
    private Seq<UnitType> units;
    private WeeklyModifier weeklyModifier;
    private UnitType[][] species ;
    private  int speciesCount ;
    public enum WeeklyModifier {
        NORMAL,//normal generation
        AIR_ONLY,//only air enemies
        HIGH_SHIELDS,//all enemies have shields
        SWARM,//double the enemies per wave
        ELITE_WAVE,//only bosses
        CHAOS      // random species per wave
    }

    public InfiniteWaveClass(){
        this.rand = new Rand(Vars.getWeeklySeed());
        this.baseDifficulty = 1;
        this.speciesCount=1;
        this.weeklyModifier = WeeklyModifier.NORMAL;
        species=new UnitType[][] {
                {dagger, flare,pulsar, crawler},
                {nova, atrax, mace, vela, minke},
                {bryde, scepter, spiroct, arkyid, toxopid},
                {quasar, corvus, reign, sei, omura},
                {oxynoe, cyerce, aegires, navanax}, //reusa intentionally left out as it cannot damage the core properly
                {fortress, horizon, zenith, quad , antumbra, quad , eclipse}
        };
    }
    private double waveDifficulty(int wave){
        return  (baseDifficulty + wave/60f+Math.pow(wave,0.30)*0.1);

    }
    public Seq<SpawnGroup> generate(int wave){

        Seq<SpawnGroup> spawns = new Seq<>();

        float difficulty = (float)waveDifficulty(wave);

        int amount = 1 + (int)(difficulty * 2f);

        if(weeklyModifier == WeeklyModifier.SWARM){
            amount *= 2;
        }
        float shield = Math.max((wave - 5) * (2f + difficulty * 1.5f), 0f);
        UnitType[] selected = getSpeciesForWave(wave, difficulty);


        float spawnChance = (wave *2) + (difficulty *5);

        if (rand.random(0, 100) < spawnChance) {
            speciesCount++;
        }
        amount = Mathf.ceil(amount / Mathf.sqrt(speciesCount));

        if(wave % 40 == 0){
            speciesCount=1;


            int index = rand.random(999999);
            index %= selected.length;
            UnitType boss    = selected[index];

            spawns.add(new SpawnGroup(boss){{
                unitAmount = 1;
                begin = wave;
                end = wave;
                spacing = 30;
                shields = 500 + difficulty * 100f;
                effect = mindustry.content.StatusEffects.boss;
            }});
        }
        for(int i = 0; i < speciesCount; i++){
            int index = rand.random(999999);
            index %= selected.length;
            UnitType type = selected[index];


            int finalAmount = amount;
            spawns.add(new SpawnGroup(type){{
                unitAmount = finalAmount;
                begin = wave;
                end = wave;
                shields = shield;
                max = 30;
            }});
        }
        return spawns;
    }

    private UnitType[] getSpeciesForWave(int wave,float difficulty){
        UnitType[] out=new UnitType[species.length];
        switch(weeklyModifier){
            case NORMAL:
             out=   selectSpeciesDefault(difficulty,wave);
                break;
                case AIR_ONLY:
                out=  Structs.filter(UnitType.class, selectSpeciesDefault(difficulty,wave), u -> u.flying);
                break;
                case CHAOS:
                  out= species[rand.random(species.length - 1)];
                    break;
                    case ELITE_WAVE:

            default:
                out= null;
                break;
        }
        return out ;

    }
  public Seq<SpawnGroup> buildSpawnGroups(int startWave, int count){
        rand.setSeed(Vars.getWeeklySeed());
        speciesCount=1;
        Seq<SpawnGroup> all = new Seq<>();
        for(int wave = startWave; wave < startWave + count; wave++){
            all.addAll(generate(wave));
        }

        return all;
    }
    private UnitType[] selectSpeciesDefault( float difficulty, int wave){

        if(wave < 10){

            return new UnitType[]{
                    UnitTypes.dagger,
                    UnitTypes.crawler,
                    UnitTypes.nova,
                    UnitTypes.flare,

            };
        }

        if(wave < 25){

            return new UnitType[]{

                    UnitTypes.dagger,
                    UnitTypes.mace,
                    UnitTypes.crawler,
                    UnitTypes.atrax,

                    UnitTypes.nova,
                    UnitTypes.pulsar,

                    UnitTypes.flare,
                    UnitTypes.horizon,

            };
        }

        if(wave < 45){

            return new UnitType[]{

                    UnitTypes.mace,
                    UnitTypes.fortress,
                    UnitTypes.crawler,
                    UnitTypes.atrax,
                    UnitTypes.spiroct,

                    UnitTypes.nova,
                    UnitTypes.pulsar,
                    UnitTypes.quasar,

                    UnitTypes.flare,
                    UnitTypes.horizon,
                    UnitTypes.zenith,

            };
        }

        if(wave < 70){

            return new UnitType[]{

                    UnitTypes.fortress,
                    UnitTypes.scepter,
                    UnitTypes.spiroct,
                    UnitTypes.arkyid,

                    UnitTypes.toxopid,

                    UnitTypes.quasar,
                    UnitTypes.vela,

                    UnitTypes.horizon,
                    UnitTypes.zenith,
                    UnitTypes.quad,

            };
        }

        if(wave < 100){

            return new UnitType[]{

                    UnitTypes.scepter,
                    UnitTypes.reign,
                    UnitTypes.arkyid,
                    UnitTypes.toxopid,

                    UnitTypes.quasar,
                    UnitTypes.vela,
                    UnitTypes.corvus,

                    UnitTypes.zenith,
                    UnitTypes.quad,
                    UnitTypes.antumbra,

                    UnitTypes.sei,
                    UnitTypes.omura,
                    UnitTypes.aegires,
                    UnitTypes.navanax
            };
        }


        return new UnitType[]{

                UnitTypes.dagger,
                UnitTypes.mace,
                UnitTypes.fortress,
                UnitTypes.scepter,
                UnitTypes.reign,


                UnitTypes.nova,
                UnitTypes.pulsar,
                UnitTypes.quasar,
                UnitTypes.vela,
                UnitTypes.corvus,


                UnitTypes.crawler,
                UnitTypes.atrax,
                UnitTypes.spiroct,
                UnitTypes.arkyid,
                UnitTypes.toxopid,




                UnitTypes.flare,
                UnitTypes.horizon,
                UnitTypes.zenith,
                UnitTypes.quad,
                UnitTypes.antumbra,
                UnitTypes.eclipse
        };
    }

}
