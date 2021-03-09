CREATE CONSTRAINT ON (node:`UNIQUE IMPORT LABEL`) ASSERT (node.`UNIQUE IMPORT ID`) IS UNIQUE;
UNWIND [
    {_id:9, properties:{interval:["1960—Now"], title:"Name"}},
    {_id:10, properties:{interval:["1980—Now"], title:"Name"}},
    {_id:11, properties:{interval:["1960—Now"], title:"Name"}},
    {_id:12, properties:{interval:["1977—Now"], title:"Name"}},
    {_id:13, properties:{interval:["1960—Now"], title:"Name"}},
    {_id:14, properties:{interval:["2001—Now"], title:"Name"}},
    {_id:15, properties:{interval:["1960—Now"], title:"Name"}},
    {_id:16, properties:{interval:["1982—Now"], title:"Name"}}] as row
CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row._id}) SET n += row.properties SET n:Attribute;
UNWIND [{_id:1, properties:{interval:["1960—Now"], title:"Airport", id: 1}},
 {_id:2, properties:{interval:["1960—Now"], title:"Airport", id: 2}},
 {_id:3, properties:{interval:["1960—Now"], title:"Airport", id: 3}},
 {_id:4, properties:{interval:["1960—Now"], title:"Airport", id: 4}},
 {_id:5, properties:{interval:["1960—Now"], title:"City", id: 5}},
 {_id:6, properties:{interval:["1960—Now"], title:"City", id: 6}},
 {_id:7, properties:{interval:["1960—Now"], title:"City", id: 7}},
 {_id:8, properties:{interval:["1960—Now"], title:"City", id: 8}}] as row
CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row._id}) SET n += row.properties SET n:Object;
UNWIND [{_id:17, properties:{interval:["1960—Now"], value:"A Airport"}},
 {_id:18, properties:{interval:["1960—Now"], value:"B Airport"}},
 {_id:19, properties:{interval:["1960—Now"], value:"C Airport"}},
 {_id:20, properties:{interval:["1960—Now"], value:"D Airport"}},
 {_id:21, properties:{interval:["1960—Now"], value:"A"}},
 {_id:22, properties:{interval:["1960—Now"], value:"B"}},
 {_id:23, properties:{interval:["1960—Now"], value:"C"}},
 {_id:24, properties:{interval:["1960—Now"], value:"D"}}] as row
CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row._id}) SET n += row.properties SET n:Value;
UNWIND [{start: {_id:1}, end: {_id:5}, properties:{interval:["1960—Now"]}},
 {start: {_id:2}, end: {_id:6}, properties:{interval:["1960—Now"]}},
 {start: {_id:3}, end: {_id:7}, properties:{interval:["1960—Now"]}},
 {start: {_id:4}, end: {_id:8}, properties:{interval:["1960—Now"]}}] as row
MATCH (start:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.end._id})
CREATE (start)-[r:LocatedAt]->(end) SET r += row.properties;
UNWIND [{start: {_id:3293}, end: {_id:3294}, properties:{}},
 {start: {_id:1}, end: {_id:9}, properties:{}},
 {start: {_id:2}, end: {_id:10}, properties:{}},
 {start: {_id:3}, end: {_id:11}, properties:{}},
 {start: {_id:4}, end: {_id:12}, properties:{}},
 {start: {_id:5}, end: {_id:13}, properties:{}},
 {start: {_id:6}, end: {_id:14}, properties:{}},
 {start: {_id:7}, end: {_id:15}, properties:{}},
 {start: {_id:8}, end: {_id:16}, properties:{}}] as row
MATCH (start:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.end._id})
CREATE (start)-[r:Edge]->(end) SET r += row.properties;
UNWIND [{start: {_id:1}, end: {_id:2}, properties:{interval:["2018-02-01—2018-02-03","2018-02-04—2018-02-06"], flightNumber:"FS4692,MZ7782"}},
 {start: {_id:1}, end: {_id:3}, properties:{interval:["2018-02-10—2018-02-13"], flightNumber:"EZ8150"}},
 {start: {_id:1}, end: {_id:4}, properties:{interval:["2018-02-06—2018-02-08"], flightNumber:"YU4304"}},
 {start: {_id:2}, end: {_id:1}, properties:{interval:["2018-02-05—2018-02-07"], flightNumber:"BS3140"}},
 {start: {_id:2}, end: {_id:3}, properties:{interval:["2018-02-02—2018-02-03","2018-02-05—2018-02-06","2018-02-06—2018-02-07"], flightNumber:"NP7940,DB4780,FN0671"}},
 {start: {_id:3}, end: {_id:2}, properties:{interval:["2018-02-02—2018-02-03"], flightNumber:"GI2787"}},
 {start: {_id:3}, end: {_id:4}, properties:{interval:["2018-02-03—2018-02-05","2018-02-07—2018-02-09"], flightNumber:"HZ8963,GK1809"}},
 {start: {_id:4}, end: {_id:1}, properties:{interval:["2018-02-01—2018-02-02"], flightNumber:"EJ7576"}},
 {start: {_id:4}, end: {_id:2}, properties:{interval:["2018-02-07—2018-02-25"], flightNumber:"PI6705"}}] as row
MATCH (start:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.end._id})
CREATE (start)-[r:Flight]->(end) SET r += row.properties;
UNWIND [{start: {_id:9}, end: {_id:17}, properties:{}},
 {start: {_id:10}, end: {_id:18}, properties:{}},
 {start: {_id:11}, end: {_id:19}, properties:{}},
 {start: {_id:12}, end: {_id:20}, properties:{}},
 {start: {_id:13}, end: {_id:21}, properties:{}},
 {start: {_id:14}, end: {_id:22}, properties:{}},
 {start: {_id:15}, end: {_id:23}, properties:{}},
 {start: {_id:16}, end: {_id:24}, properties:{}}] as row
MATCH (start:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.end._id})
CREATE (start)-[r:Edge]->(end) SET r += row.properties;
MATCH (n:`UNIQUE IMPORT LABEL`)  WITH n LIMIT 20000 REMOVE n:`UNIQUE IMPORT LABEL` REMOVE n.`UNIQUE IMPORT ID`;
DROP CONSTRAINT ON (node:`UNIQUE IMPORT LABEL`) ASSERT (node.`UNIQUE IMPORT ID`) IS UNIQUE;
