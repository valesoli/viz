// GQL https://db-engines.com/en/blog_post/78
// ANTLR Tutorial 1 https://tomassetti.me/antlr-mega-tutorial/#java-setup
// ANTLR Tutorial 2 https://www.baeldung.com/java-antlr

grammar TempoGraph;

/*
 * Parser Rules
 */

query               :   query_body (WS temporal_operators)? (WS skip_clause)? (WS limit_clause)? WS? EOF ;

query_body          :   WS? select_clause WS match_clause (WS where_clause)? ;

temporal_operators  :   when_clause | snapshot_clause | between_clause ;

select_clause       :   SELECT WS (((select_exp WS? COMMA WS?)* select_exp) | TIMES) ;

match_clause        :   MATCH WS ((path | f_call) WS? COMMA WS?)* (path | f_call) ;

where_clause        :   WHERE WS where_conditions;

where_conditions    :   where_conditions where_connector where_conditions
                    |   (LPAREN WS? where_conditions WS? RPAREN)
                    |   condition ;

when_clause         :   WHEN WS match_clause (WS where_clause)? ;

snapshot_clause     :   SNAPSHOT WS time_value ;

between_clause      :   BETWEEN WS time_value WS AND WS time_value ;

limit_clause        :   LIMIT WS U_INTEGER ;

skip_clause         :   SKIP_S WS U_INTEGER ;

f_call              :   WORD WS? EQ WS? (cpath | latest | fastest | earliest | shortest) ;

cpath               :   (CPATH | PAIRCPATH) WS? LPAREN WS? endpoints_args (WS? COMMA WS? interval_arg)? WS? RPAREN ;

latest              :   (LATESTDEPARTURE | LATESTARRIVAL) WS? LPAREN WS? endpoints_args (WS? COMMA WS? time_value)? WS? RPAREN ;

fastest             :   FASTEST WS? LPAREN WS? endpoints_args WS? RPAREN ;

earliest            :   EARLIEST WS? LPAREN WS? endpoints_args WS? RPAREN ;

shortest            :   SHORTEST WS? LPAREN WS? endpoints_args WS? RPAREN ;

id_call             :   ID LPAREN WORD RPAREN ;

endpoints_args      :   object relation_edge object ;

interval_arg        :   time_value WS? COMMA WS? time_value ;

path                :   object ((direct_edge | relation_edge) object)* ;

direct_edge         :   WS? (FULL_RIGHT | FULL_LEFT | FULL_EDGE) WS? ;

relation_edge       :   WS? ((DASH edge_def RIGHT) | (LEFT edge_def DASH) | (DASH edge_def DASH)) WS? ;

object              :   object_variable | object_def ;

object_variable     :   LPAREN WORD RPAREN ;

object_def          :   LPAREN WORD? COLON WORD RPAREN ;

edge_def            :   WS? LBRACKET WORD? COLON WORD variable_length? RBRACKET WS? ;

variable_length     :   TIMES (U_INTEGER | (U_INTEGER? DOUBLE_DOT U_INTEGER))? ;

attr                :   WORD DOT WORD ;

select_exp          :   (attr alias?) | WORD | (select_fstruct alias?) | (id_call alias?) | (property_access alias?) | (aggr alias?) | (utils alias?);

select_fstruct      :   ((WORD DOT WORD LBRACKET U_INTEGER RBRACKET) | (index_accessor LPAREN WORD DOT WORD RPAREN)) (DOT WORD (LBRACKET U_INTEGER RBRACKET)?)* ;

index_accessor      :   HEAD | LAST ;

aggr                :   sum ;

sum                 :   SUM WS? LPAREN WS? (attr | select_fstruct | utils | WORD) WS? RPAREN ;

utils               :   size ;

size                :   SIZE WS? LPAREN WS? (attr | select_fstruct | WORD) WS? RPAREN ;

property_access     :   WORD LBRACKET (WORD | ID) RBRACKET ;

alias               :   WS AS WS WORD ;

where_connector     :   WS (AND | OR) WS ;

condition           :   (value WS? relop WS? value) | where_fcall_bool ;

where_fcall_bool    :   cpath ;

value               :   math | STRING | time_value ;

time_value          :   YEAR | YEAR_MONTH | DATE | DATETIME ;

math                :   math WS? POW WS? math
                    |   math WS? (PLUS | DASH | TIMES | DIV) WS? math
                    |   LPAREN WS? math WS? RPAREN
                    |   (PLUS | DASH)* WS? atom ;

atom                :   scientific
                    |   id_call
                    |   attr
                    |   property_access
                    |   WORD ;

scientific          :   (DOUBLE | U_INTEGER) SCIENTIFIC_NUMBER? ;

relop               :   EQ
                    |   NEQ
                    |   GT
                    |   GTE
                    |   LT
                    |   LTE ;

/*
 * Lexer Rules
 */

fragment DIGIT      :   [0-9] ;
fragment A          :   'A' | 'a' ;
fragment B          :   'B' | 'b' ;
fragment C          :   'C' | 'c' ;
fragment D          :   'D' | 'd' ;
fragment E          :   'E' | 'e' ;
fragment F          :   'F' | 'f' ;
fragment G          :   'G' | 'g' ;
fragment H          :   'H' | 'h' ;
fragment I          :   'I' | 'i' ;
fragment K          :   'K' | 'k' ;
fragment L          :   'L' | 'l' ;
fragment M          :   'M' | 'm' ;
fragment N          :   'N' | 'n' ;
fragment O          :   'O' | 'o' ;
fragment P          :   'P' | 'p' ;
fragment R          :   'R' | 'r' ;
fragment S          :   'S' | 's' ;
fragment T          :   'T' | 't' ;
fragment U          :   'U' | 'u' ;
fragment V          :   'V' | 'v' ;
fragment W          :   'W' | 'w' ;
fragment Y          :   'Y' | 'y' ;
fragment Z          :   'Z' | 'z' ;

SIZE                :   S I Z E ;
SUM                 :   S U M ;
ID                  :   I D ;
HEAD                :   H E A D ;
LAST                :   L A S T ;
LIMIT               :   L I M I T ;
SKIP_S              :   S K I P ;
CPATH               :   C P A T H ;
PAIRCPATH           :   P A I R C P A T H ;
EARLIEST            :   E A R L I E S T P A T H ;
FASTEST             :   F A S T E S T P A T H ;
SHORTEST            :   S H O R T E S T P A T H ;
LATESTDEPARTURE     :   L A T E S T D E P A R T U R E P A T H ;
LATESTARRIVAL       :   L A T E S T A R R I V A L P A T H ;
SELECT              :   S E L E C T ;
AS                  :   A S ;
MATCH               :   M A T C H ;
WHERE               :   W H E R E ;
WHEN                :   W H E N ;
SNAPSHOT            :   S N A P S H O T ;
BETWEEN             :   B E T W E E N ;
AND                 :   A N D ;
OR                  :   O R ;
YEAR                :   QUOTE DIGIT DIGIT DIGIT DIGIT QUOTE ;
YEAR_MONTH          :   QUOTE DIGIT DIGIT DIGIT DIGIT DASH DIGIT DIGIT QUOTE ;
DATE                :   QUOTE DIGIT DIGIT DIGIT DIGIT DASH DIGIT DIGIT DASH DIGIT DIGIT QUOTE ;
DATETIME            :   QUOTE DIGIT DIGIT DIGIT DIGIT DASH DIGIT DIGIT DASH DIGIT DIGIT ' ' DIGIT DIGIT COLON DIGIT DIGIT QUOTE ;
RIGHT               :   '->' ;
LEFT                :   '<-' ;
FULL_RIGHT          :   '-->' ;
FULL_LEFT           :   '<--' ;
FULL_EDGE           :   '--' ;
LPAREN              :   '(' ;
RPAREN              :   ')' ;
LBRACKET            :   '[' ;
RBRACKET            :   ']' ;
DOT                 :   '.' ;
DOUBLE_DOT          :   '..' ;
COLON               :   ':' ;
COMMA               :   ',' ;
QUOTE               :   '\'' ;
DASH                :   '-' ;
TIMES               :   '*' ;
DIV                 :   '/' ;
PLUS                :   '+' ;
GT                  :   '>' ;
GTE                 :   '>=' ;
LT                  :   '<' ;
LTE                 :   '<=' ;
EQ                  :   '=' ;
NEQ                 :   '<>' ;
POW                 :   '^' ;
DOUBLE              :   DIGIT* '.' DIGIT+ ;
U_INTEGER           :   DIGIT+ ;
SCIENTIFIC_NUMBER   :   'E' DASH? U_INTEGER ;
WORD                :   ([a-z] | [A-Z])([a-z] | [A-Z] | DIGIT+ | '_' )*;
STRING              :   QUOTE .*? QUOTE;
WS                  :   [ \t\r\n]+ ;
BLOCK_COMMENT       :   '/*' .*? '*/' WS? -> skip;