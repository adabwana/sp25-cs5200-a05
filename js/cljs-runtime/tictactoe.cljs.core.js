goog.provide('tictactoe.cljs.core');
/**
 * Initialize a new game with optional custom configuration.
 */
tictactoe.cljs.core.init_game = (function tictactoe$cljs$core$init_game(var_args){
var G__22995 = arguments.length;
switch (G__22995) {
case 0:
return tictactoe.cljs.core.init_game.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return tictactoe.cljs.core.init_game.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(tictactoe.cljs.core.init_game.cljs$core$IFn$_invoke$arity$0 = (function (){
return tictactoe.cljs.core.init_game.cljs$core$IFn$_invoke$arity$1(tictactoe.cljs.config.settings.get_config(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"default","default",-1987822328)], null)));
}));

(tictactoe.cljs.core.init_game.cljs$core$IFn$_invoke$arity$1 = (function (custom_config){
var config = tictactoe.cljs.config.settings.merge_config(custom_config);
return new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"board","board",-1907017633),tictactoe.cljs.board.init_board(new cljs.core.Keyword(null,"board-size","board-size",140730505).cljs$core$IFn$_invoke$arity$1(config)),new cljs.core.Keyword(null,"current-player","current-player",-970625153),"X",new cljs.core.Keyword(null,"game-over?","game-over?",432859304),false,new cljs.core.Keyword(null,"winner","winner",714604679),null,new cljs.core.Keyword(null,"config","config",994861415),config], null);
}));

(tictactoe.cljs.core.init_game.cljs$lang$maxFixedArity = 1);

/**
 * Switch the current player.
 */
tictactoe.cljs.core.switch_player = (function tictactoe$cljs$core$switch_player(current_player){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_player,"X")){
return "O";
} else {
return "X";
}
});
/**
 * Check if the game is over and return updated state.
 */
tictactoe.cljs.core.check_game_status = (function tictactoe$cljs$core$check_game_status(state){
var map__22996 = state;
var map__22996__$1 = cljs.core.__destructure_map(map__22996);
var board = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22996__$1,new cljs.core.Keyword(null,"board","board",-1907017633));
var current_player = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22996__$1,new cljs.core.Keyword(null,"current-player","current-player",-970625153));
var config = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22996__$1,new cljs.core.Keyword(null,"config","config",994861415));
var map__22997 = config;
var map__22997__$1 = cljs.core.__destructure_map(map__22997);
var win_length = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22997__$1,new cljs.core.Keyword(null,"win-length","win-length",547963036));
if(cljs.core.truth_(tictactoe.cljs.board.check_winner(board,current_player,win_length))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(state,new cljs.core.Keyword(null,"game-over?","game-over?",432859304),true,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"winner","winner",714604679),current_player], 0));
} else {
if(tictactoe.cljs.board.board_full_QMARK_(board)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(state,new cljs.core.Keyword(null,"game-over?","game-over?",432859304),true,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"winner","winner",714604679),null], 0));
} else {
return state;

}
}
});
/**
 * Process AI move if it's AI's turn.
 */
tictactoe.cljs.core.process_ai_move = (function tictactoe$cljs$core$process_ai_move(state){
var map__23005 = state;
var map__23005__$1 = cljs.core.__destructure_map(map__23005);
var board = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23005__$1,new cljs.core.Keyword(null,"board","board",-1907017633));
var config = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23005__$1,new cljs.core.Keyword(null,"config","config",994861415));
var map__23006 = config;
var map__23006__$1 = cljs.core.__destructure_map(map__23006);
var ai_enabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23006__$1,new cljs.core.Keyword(null,"ai-enabled","ai-enabled",-1594045602));
var ai_player = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23006__$1,new cljs.core.Keyword(null,"ai-player","ai-player",1861644924));
var win_length = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23006__$1,new cljs.core.Keyword(null,"win-length","win-length",547963036));
if(cljs.core.truth_((function (){var and__5000__auto__ = ai_enabled;
if(cljs.core.truth_(and__5000__auto__)){
return ((cljs.core.not(new cljs.core.Keyword(null,"game-over?","game-over?",432859304).cljs$core$IFn$_invoke$arity$1(state))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"current-player","current-player",-970625153).cljs$core$IFn$_invoke$arity$1(state),ai_player)));
} else {
return and__5000__auto__;
}
})())){
var vec__23013 = tictactoe.cljs.ai.engine.get_best_move(board,ai_player,win_length);
var ai_row = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23013,(0),null);
var ai_col = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23013,(1),null);
if(cljs.core.truth_((function (){var and__5000__auto__ = ai_row;
if(cljs.core.truth_(and__5000__auto__)){
return ai_col;
} else {
return and__5000__auto__;
}
})())){
var new_board = tictactoe.cljs.board.place_mark(board,ai_row,ai_col,ai_player);
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(tictactoe.cljs.core.check_game_status(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(state,new cljs.core.Keyword(null,"board","board",-1907017633),new_board)),new cljs.core.Keyword(null,"current-player","current-player",-970625153),tictactoe.cljs.core.switch_player);
} else {
return state;
}
} else {
return state;
}
});
/**
 * Make a move and return the updated game state.
 */
tictactoe.cljs.core.make_move = (function tictactoe$cljs$core$make_move(state,row,col){
var map__23016 = state;
var map__23016__$1 = cljs.core.__destructure_map(map__23016);
var board = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23016__$1,new cljs.core.Keyword(null,"board","board",-1907017633));
var current_player = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23016__$1,new cljs.core.Keyword(null,"current-player","current-player",-970625153));
var game_over_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23016__$1,new cljs.core.Keyword(null,"game-over?","game-over?",432859304));
if(((cljs.core.not(game_over_QMARK_)) && (tictactoe.cljs.board.valid_move_QMARK_(board,row,col)))){
var new_board = tictactoe.cljs.board.place_mark(board,row,col,current_player);
return tictactoe.cljs.core.process_ai_move(cljs.core.update.cljs$core$IFn$_invoke$arity$3(tictactoe.cljs.core.check_game_status(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(state,new cljs.core.Keyword(null,"board","board",-1907017633),new_board)),new cljs.core.Keyword(null,"current-player","current-player",-970625153),tictactoe.cljs.core.switch_player));
} else {
return state;
}
});

//# sourceMappingURL=tictactoe.cljs.core.js.map
