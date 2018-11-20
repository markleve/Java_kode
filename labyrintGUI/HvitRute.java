
public class HvitRute extends Rute {

  public HvitRute(int rad, int kolonne) {
    super(rad, kolonne);
  }

  @Override
  public void gaa(Rute forrigeRute, String vei, Liste<String> utveier) {
    if(paaVeien) { return; }
    paaVeien = true;

    vei += "(" + (kolonne+1) + ", " + (rad+1) + ") --> ";

    if(forrigeRute != naboNord) { naboNord.gaa(this, vei, utveier); }
    if(forrigeRute != naboOst) { naboOst.gaa(this, vei, utveier); }
    if(forrigeRute != naboSyd) { naboSyd.gaa(this, vei, utveier); }
    if(forrigeRute != naboVest) { naboVest.gaa(this, vei, utveier); }

    paaVeien = false;
  }

  @Override
  public char tilTegn() {
    return ' ';
  }

  @Override
  public String toString() {
    return tilTegn() + " [" + rad + "][" + kolonne + "]";
  }
}
