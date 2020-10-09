import { normalizeInterval } from "core/services/graphBuildingService";
import { isInInterval } from "core/services/graphBuildingService";

function testingIntervals() {
  let interval = [1990, 2010];
  let positiveTestingIntervals = [
    //Inicio en inicio intervalo
    "1990—2000",
    "1990—2012",
    "1990—Now",
    "1990—2010",

    //Inicio en pre intervalo
    "1980—2000",
    "1980—2012",
    "1980—Now",
    "1980—2010",

    //Inicio en post intervalo
    "1995—2000",
    "1995—2012",
    "1995—Now",
    "1995—2010",

    //Inicio pre intervalo y Fin en inicio intervalo
    "1950—1990",
    //Inicio y fin en inicio intervalo
    "1990—1990",

    //Inicio en fin intervalo
    "2010—2012",
    "2010—Now",
    //Inicio y fin en fin intervalo
    "2010—2010",
  ];
  let negativeTestingIntervals = [
    //Pre inicio intervalo
    "1980—1985",
    "1980—1980",

    //Post fin intervalo
    "2012—2012",
    "2012—2014",
    "2012—Now",
  ];

  for (let i = 0; i < positiveTestingIntervals.length; i++) {
    it("positive intervalsTest " + i, () => {
      expect(isInInterval(positiveTestingIntervals[i], interval)).toEqual(true);
    });
  }
  for (let i = 0; i < negativeTestingIntervals.length; i++) {
    it("negative intervalsTest " + negativeTestingIntervals[i], () => {
      expect(isInInterval(negativeTestingIntervals[i], interval)).toEqual(
        false
      );
    });
  }
}

function testingParseIntervals(){
  let intervals = [
    ["1990","1990"],
    ["1990-08","1991-09"],
    ["1990-08-12", "1991-09-01"],
    ["1990-08-12 11:00", "1991-09-01 12:00"]
  ]
  let expectedOuts = [
    ["1990-01-01 00:00", "1990-12-31 23:59"],
    ["1990-08-01 00:00", "1990-09-31 23:59"],
    ["1990-08-12 00:00", "1990-09-01 23:59"],
    ["1990-08-12 11:00", "1991-09-01 12:00"]
  ]

  for (let i=0; i<intervals.length ; i++){
    it("Parsing Intervals Test " + i, () => {
      expect(normalizeInterval(intervals[i])).toEqual(
        [Date.parse(expectedOuts[i][0]),Date.parse(expectedOuts[i][1])]
      );
    });
  }
}


//TESTING SUITS
// testingIntervals();
testingParseIntervals();