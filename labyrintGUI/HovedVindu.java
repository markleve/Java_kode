import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.text.Text;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.collections.FXCollections;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCode;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;



public class HovedVindu extends Stage {

  private Application applicationObjekt;

  /*  Variablene som sier noe om størrelsen på de ulike Objektene */
  private static final int VINDU_BREDDE = 850;
  private static final int VINDU_HOYDE = 650;

  private static final double TOPPBOKS_HOYDE = VINDU_HOYDE*0.05;
  private static final double BUNNBOKS_HOYDE = VINDU_HOYDE*0.03;
  private static final double MIDTBOKS_HOYDE = VINDU_HOYDE-TOPPBOKS_HOYDE-BUNNBOKS_HOYDE;

  private static final double VENSTREBOKS_BREDDE = VINDU_BREDDE*0.18;
  private static final double HOYREBOKS_BREDDE = VINDU_BREDDE*0.03;
  private static final double MIDTBOKS_BREDDE = VINDU_BREDDE-VENSTREBOKS_BREDDE-HOYREBOKS_BREDDE;

  private static final double KNAPP_BREDDE = VENSTREBOKS_BREDDE*0.90;

  /*  Objektene som skal inn i scenen opprettes under  */
  // Hovedscene (foreldre node)
  private BorderPane rotNode;

  // Toppboks (barne node)
  private HBox toppBoks;
  private MenuBar menuBarEn = new MenuBar();
  private MenuBar menuBarTo = new MenuBar();
  private Menu menuFile = new Menu("File");
  private MenuItem opneFil = new MenuItem("Åpne fil");
  private MenuItem clearFil = new MenuItem("Clear");
  private MenuItem lukkFil = new MenuItem("Lukk");

  // Ønsker å legge til en "Edit" og "View" knapp.
  //    - lagre løsning (korteste utvei)
  //    - få opp nytt vindu for å lage egen labyrint
  
  //private Menu menuEdit = new Menu("Edit");
  //private Menu menuView = new Menu("View");

  private Menu menuHelp = new Menu("Hjelp");
  private MenuItem helpTekst = new MenuItem("Informasjon");

  // Ønsker at denne meldingen skal komme fram når jeg trykker på hjelp
  private Text help = new Text("\nLabyrint program\n\nLabyrint programmet er et program hvor du kan åpne en Labyrint fil \nog finne veien ut fra ulike deler av labyrinten.\n\nEn labyrint fil åpnes ved å klikke \"File\" og \"Åpne fil\" i menybaren. \nDen valgte labyrinten vil vises i hovedvinduet og du kan klikke på ruten \ndu ønsker å finne utveien fra. Klikk på så mange ruter du ønsker. \n\nNår en labyrint åpnes vil en boks til venstre komme fram. Her er det flere \nvalg:\n- Se antall løsninger\n- Velge løsning nummer du ønsker å se\n- Se den korteste løsningen og antall skritt \n- Se den lengste løsningen og antall skritt\n\nProgrammet kan løse både sykliske og ikke sykliske labyrinter. Dersom du \nønsker å lage din egen labyrint, pass på at den følger formatet de \nvedlagte labyrintene følger.\n\nHusk å sjekke bunn boksen for eventuelle beskjeder.\n\n");

  // Senterboks (barne node)
  private LabyrintNode labyrintGridPane = null;

  // Venstre boks (barne node)
  private VBox venstreBoks;
  private Button resetLabyrintKnapp = new Button("Reset labyrint");
  private Text visAntLosninger = new Text("Antall løsninger: ");
  private Text velgLosning = new Text("Velg løsning:");
  private ComboBox<String> velgLosningListe = new ComboBox<String>();
  private VBox kortLosning;
  private Button kortestLosningKnapp = new Button("Korteste løsning");
  private Text kortLosningLengde = new Text("");
  private VBox langLosning;
  private Button lengstLosningKnapp = new Button("Lengste løsning");
  private Text langLosningLengde = new Text("");

  // Hoyre boks (barne node)
  private VBox hoyreBoks;

  // Bunnboks (barne node - vis tekst linje)
  private HBox bunnBoks;
  private Text beskjedTilBruker = new Text("");

  public HovedVindu(Application appObjekt) {
    this.applicationObjekt = appObjekt;
    setTitle("Finner utveier fra labyrint");
    rotNode = new BorderPane();
    byggOppScene();
    opprettHandlinger();
    setScene(new Scene(rotNode));
  }

  public void byggOppScene() {
    toppBox();
    venstreBox();
    hoyreBox();
    bunnBox();
    rotNode.setTop(toppBoks);
    rotNode.setBottom(bunnBoks);
  }

  public void toppBox() {
    // Oppretter toppboksen
    lukkFil.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN));
    opneFil.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
    clearFil.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN));

    menuFile.getItems().addAll(opneFil, clearFil, new SeparatorMenuItem(), lukkFil);
    menuHelp.getItems().addAll(helpTekst);
    //menuBarEn.getMenus().addAll(menuFile, menuEdit, menuView);
    menuBarEn.getMenus().addAll(menuFile);

    menuBarEn.setPrefWidth(VINDU_BREDDE*0.90);
    menuBarEn.setPrefHeight(TOPPBOKS_HOYDE);
    menuBarTo.getMenus().addAll(menuHelp);
    menuBarTo.setPrefWidth(VINDU_BREDDE*0.10);
    menuBarTo.setPrefHeight(TOPPBOKS_HOYDE);
    toppBoks = new HBox(menuBarEn, menuBarTo);
  }

  public void venstreBox() {
    // Oppretter venstre boks
    resetLabyrintKnapp.setPrefWidth(KNAPP_BREDDE);
    velgLosningListe.setPrefWidth(KNAPP_BREDDE);
    kortestLosningKnapp.setPrefWidth(KNAPP_BREDDE);
    lengstLosningKnapp.setPrefWidth(KNAPP_BREDDE);

    kortLosning = new VBox(kortestLosningKnapp);
    kortLosning.setSpacing(10);
    langLosning = new VBox(lengstLosningKnapp);
    langLosning.setSpacing(10);

    venstreBoks = new VBox(resetLabyrintKnapp, new Separator(), visAntLosninger,
                           new Separator(), velgLosning, velgLosningListe, new Separator(),
                           kortLosning, new Separator(), langLosning);
    venstreBoks.setStyle("-fx-background-color: GAINSBORO; -fx-padding: 10 10 10 10; -fx-spacing: 10;-fx-border-color: LIGHTGREY; -fx-border-width: 0 2 0 2");
    venstreBoks.setPrefWidth(VENSTREBOKS_BREDDE);
    venstreBoks.setPrefHeight(MIDTBOKS_HOYDE);
  }

  public void hoyreBox() {
    // Oppretter hoyre boksen
    hoyreBoks = new VBox();
    hoyreBoks.setStyle("-fx-background-color: GAINSBORO; -fx-padding: 10 10 10 10; -fx-spacing: 10;");
    hoyreBoks.setPrefWidth(HOYREBOKS_BREDDE);
    hoyreBoks.setPrefHeight(MIDTBOKS_HOYDE);
  }

  public void bunnBox() {
    // Oppretter bunnboksen
    bunnBoks = new HBox(beskjedTilBruker);
    bunnBoks.setStyle("-fx-background-color: GAINSBORO;  -fx-padding: 10 10 10 10; -fx-spacing: 10; -fx-border-color: LIGHTGREY; -fx-border-width: 2px;");
    bunnBoks.setPrefWidth(VINDU_BREDDE);
    bunnBoks.setPrefHeight(BUNNBOKS_HOYDE);
  }

  public void opprettHandlinger() {
    HovedVindu hovedVindu = this;

    // Aktiverer handlingene til topp boksen
    opneFil.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        FileChooser filVelger = new FileChooser();
        filVelger.setTitle("Velg labyrintfil");
        File fil = filVelger.showOpenDialog(null);
        try {
          resetVenstreBoks(0, "", true, true, false);
          Labyrint labyrint = Labyrint.lesFraFil(fil);
          labyrintGridPane = new LabyrintNode(hovedVindu, MIDTBOKS_BREDDE, MIDTBOKS_HOYDE);
          labyrintGridPane.opprettLabyrinten(labyrint);
          rotNode.setCenter(labyrintGridPane);
          rotNode.setLeft(venstreBoks);
          rotNode.setRight(hoyreBoks);
          sizeToScene();
        } catch(Exception e) {
          oppdaterBunnTekst("Klarte ikke åpne filen:  " + fil);
        }
      }
    });
    lukkFil.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        System.exit(1);
      }
    });
    clearFil.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        rotNode.setRight(null);
        rotNode.setLeft(null);
        rotNode.setCenter(null);
        rotNode.setPrefHeight(TOPPBOKS_HOYDE+BUNNBOKS_HOYDE);
        sizeToScene();
      }
    });
    helpTekst.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        rotNode.setRight(null);
        rotNode.setLeft(null);
        rotNode.setCenter(help);
        //rotNode.setPrefHeight(TOPPBOKS_HOYDE+BUNNBOKS_HOYDE);
        sizeToScene();
      }
    });

    // Aktiverer handlingene til venstre boksen
    resetLabyrintKnapp.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        resetVenstreBoks(0, "", true, true, true);
      }
    });
    velgLosningListe.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue observable, String oldValue, String newValue) {
        try {
          int losningNr = Integer.parseInt(newValue);
          labyrintGridPane.visLosningListe(losningNr-1);
        } catch(Exception e) {}
      }
    });
    kortestLosningKnapp.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        labyrintGridPane.visKortesteUtvei();
        int lengde = labyrintGridPane.lengdeKortesteUtvei();
        kortLosningLengde.setText("Lengde: " + lengde);
        kortLosning.getChildren().add(kortLosningLengde);
      }
    });
    lengstLosningKnapp.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        labyrintGridPane.visLengsteUtvei();
        int lengde = labyrintGridPane.lengdeLengsteUtvei();
        langLosningLengde.setText("Lengde: " + lengde);
        langLosning.getChildren().add(langLosningLengde);
      }
    });
  }

  public void settLosningListe(String[] losninger) {
    velgLosningListe.setItems(FXCollections.observableArrayList(losninger));
  }

  public void oppdaterAntLosninger(int antUtveier) {
    visAntLosninger.setText("Antall løsninger: " + antUtveier);
  }

  public void oppdaterBunnTekst(String beskjeden) {
    beskjedTilBruker.setText(beskjeden);
  }

  public void oppdaterLosningViste() {
    if (kortLosning.getChildren().size() > 1) {
      kortLosning.getChildren().remove(1);
    }
    if (langLosning.getChildren().size() > 1) {
      langLosning.getChildren().remove(1);
    }
  }

  public void venstreBoksDisabled(boolean disabled) {
    resetLabyrintKnapp.setDisable(disabled);
    velgLosningListe.setDisable(disabled);
    kortestLosningKnapp.setDisable(disabled);
    lengstLosningKnapp.setDisable(disabled);
    if(disabled) {
      visAntLosninger.setOpacity(0.5);
      velgLosning.setOpacity(0.5);
    } else {
      visAntLosninger.setOpacity(1);
      velgLosning.setOpacity(1);
    }
  }

  public void resetVenstreBoks(int antLosninger, String bunntekst, boolean disabled, boolean clear, boolean reset) {
    oppdaterAntLosninger(antLosninger);
    oppdaterBunnTekst(bunntekst);
    venstreBoksDisabled(disabled);
    if(clear) { velgLosningListe.getItems().clear(); }
    if(reset) { labyrintGridPane.resetFarger(); }
    oppdaterLosningViste();
  }

}
