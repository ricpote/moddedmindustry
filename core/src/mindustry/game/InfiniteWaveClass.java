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
    public enum WeeklyModifier {
        NORMAL,//normal generation
        AIR_ONLY,//only air enemies
        HIGH_SHIELDS,//all enemies have shields
        SWARM,//double the enemies per wave
        CHAOS      // random species per wave
    }

    public InfiniteWaveClass(){
        this.rand = new Rand(Vars.getWeeklySeed());
        this.baseDifficulty = 1;
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
        Rand rs = new Rand(Vars.getWeeklySeed() + wave);

        Seq<SpawnGroup> spawns = new Seq<>();

        float difficulty = (float)waveDifficulty(wave);
        int speciesCount = computeSpeciesCount(wave);
        UnitType[] selected = getSpeciesForWave(wave, difficulty);


        int amount = computeAmount(wave, difficulty, speciesCount);


        float shield = Math.max((wave - 5) * (2f + difficulty * 1.5f), 0f);
        if(wave % 40 == 0){
            int idx = (int)((Vars.getWeeklySeed() * 31L + wave * 17L) % selected.length);
            UnitType boss = selected[idx];

            spawns.add(new SpawnGroup(boss){{
                unitAmount = 1;
                begin = wave - 1;
                end   = wave - 1;
                spacing = 30;
                shields = 500 + difficulty * 100f;
                effect = mindustry.content.StatusEffects.boss;
            }});
        }


        for(int i=0; i<speciesCount; i++){
            int index = (rs.random(0, selected.length-1));
            UnitType type = selected[index];

            spawns.add(new SpawnGroup(type){{
                unitAmount = amount;
                begin = wave - 1;
                end = wave - 1;
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

            default:
                out= null;
                break;
        }
        return out ;

    }
    private int computeSpeciesCount(int wave){
        Rand rs = new Rand(Vars.getWeeklySeed() + wave * 99991);
        int base = 1 + wave / 5;
        base = Mathf.clamp(base, 1, 3);
        if(rs.chance(0.15)) base = Math.min(base + 1, 3);
        return base;
    }
    private int computeAmount(int wave, float difficulty, int speciesCount){

        float base;

        if(wave < 10){

            base = Mathf.lerp(3f, 10f, wave / 10f);
        }else{
            base = Mathf.lerp(10f, 30f, Mathf.clamp((wave - 10f) / 30f, 0f, 1f));
        }
        base += difficulty * 0.7f;
        if(wave > 40){
            base += (float)Math.pow(wave - 40, 0.30f);
        }
        return Math.max(1, Mathf.ceil(base / Mathf.sqrt(speciesCount)));
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