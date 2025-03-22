goog.provide('tictactoe.cljs.ai.engine');
/**
 * Check if the position is terminal (game over)
 */
tictactoe.cljs.ai.engine.terminal_node_QMARK_ = (function tictactoe$cljs$ai$engine$terminal_node_QMARK_(board,depth,win_length){
var or__5002__auto__ = (depth === (0));
if(or__5002__auto__){
return or__5002__auto__;
} else {
var or__5002__auto____$1 = tictactoe.cljs.board.board_full_QMARK_(board);
if(or__5002__auto____$1){
return or__5002__auto____$1;
} else {
var or__5002__auto____$2 = tictactoe.cljs.board.check_winner(board,"X",win_length);
if(cljs.core.truth_(or__5002__auto____$2)){
return or__5002__auto____$2;
} else {
return tictactoe.cljs.board.check_winner(board,"O",win_length);
}
}
}
});
/**
 * Determine if we should prune the search
 */
tictactoe.cljs.ai.engine.should_prune_QMARK_ = (function tictactoe$cljs$ai$engine$should_prune_QMARK_(alpha,beta){
return (alpha >= beta);
});
/**
 * Process a single step in the minimax algorithm
 */
tictactoe.cljs.ai.engine.minimax_step = (function tictactoe$cljs$ai$engine$minimax_step(board,move,depth,is_maximizing,mark,win_length,alpha,beta){
var vec__22939 = move;
var row = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22939,(0),null);
var col = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22939,(1),null);
var opponent = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(mark,"X"))?"O":"X");
var current_mark = (cljs.core.truth_(is_maximizing)?mark:opponent);
var new_board = tictactoe.cljs.board.place_mark(board,row,col,current_mark);
if(cljs.core.truth_(tictactoe.cljs.ai.engine.terminal_node_QMARK_(new_board,depth,win_length))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tictactoe.cljs.ai.strategies.evaluate_position(new_board,mark,win_length),move], null);
} else {
var vec__22942 = (function (){var G__22945 = board;
var G__22946 = (depth - (1));
var G__22947 = cljs.core.not(is_maximizing);
var G__22948 = mark;
var G__22949 = win_length;
var G__22950 = alpha;
var G__22951 = beta;
return (tictactoe.cljs.ai.engine.minimax.cljs$core$IFn$_invoke$arity$7 ? tictactoe.cljs.ai.engine.minimax.cljs$core$IFn$_invoke$arity$7(G__22945,G__22946,G__22947,G__22948,G__22949,G__22950,G__22951) : tictactoe.cljs.ai.engine.minimax.call(null, G__22945,G__22946,G__22947,G__22948,G__22949,G__22950,G__22951));
})();
var score = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22942,(0),null);
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22942,(1),null);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [score,move], null);
}
});
/**
 * Perform alpha-beta search on a list of moves
 */
tictactoe.cljs.ai.engine.alpha_beta_search = (function tictactoe$cljs$ai$engine$alpha_beta_search(board,moves,depth,is_maximizing,mark,win_length,alpha,beta){
var map__22952 = tictactoe.cljs.config.settings.get_config(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ai","ai",760454697)], null));
var map__22952__$1 = cljs.core.__destructure_map(map__22952);
var infinity = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22952__$1,new cljs.core.Keyword(null,"infinity","infinity",-105926847));
var neg_infinity = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22952__$1,new cljs.core.Keyword(null,"neg-infinity","neg-infinity",-776629436));
var remaining_moves = moves;
var alpha__$1 = alpha;
var beta__$1 = beta;
var best_score = (cljs.core.truth_(is_maximizing)?neg_infinity:infinity);
var best_move = null;
while(true){
if(((cljs.core.empty_QMARK_(remaining_moves)) || (tictactoe.cljs.ai.engine.should_prune_QMARK_(alpha__$1,beta__$1)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [best_score,best_move], null);
} else {
var move = cljs.core.first(remaining_moves);
var vec__22966 = tictactoe.cljs.ai.engine.minimax_step(board,move,depth,is_maximizing,mark,win_length,alpha__$1,beta__$1);
var score = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22966,(0),null);
var final_move = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22966,(1),null);
var vec__22969 = (((cljs.core.truth_(is_maximizing)?(score > best_score):(score < best_score)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [score,final_move], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [best_score,best_move], null));
var new_score = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22969,(0),null);
var new_move = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22969,(1),null);
var new_alpha = (cljs.core.truth_(is_maximizing)?(function (){var x__5087__auto__ = alpha__$1;
var y__5088__auto__ = score;
return ((x__5087__auto__ > y__5088__auto__) ? x__5087__auto__ : y__5088__auto__);
})():alpha__$1);
var new_beta = (cljs.core.truth_(is_maximizing)?beta__$1:(function (){var x__5090__auto__ = beta__$1;
var y__5091__auto__ = score;
return ((x__5090__auto__ < y__5091__auto__) ? x__5090__auto__ : y__5091__auto__);
})());
var G__23000 = cljs.core.rest(remaining_moves);
var G__23001 = new_alpha;
var G__23002 = new_beta;
var G__23003 = new_score;
var G__23004 = new_move;
remaining_moves = G__23000;
alpha__$1 = G__23001;
beta__$1 = G__23002;
best_score = G__23003;
best_move = G__23004;
continue;
}
break;
}
});
/**
 * Minimax algorithm with alpha-beta pruning
 */
tictactoe.cljs.ai.engine.minimax = (function tictactoe$cljs$ai$engine$minimax(board,depth,is_maximizing,mark,win_length,alpha,beta){
if(cljs.core.truth_(tictactoe.cljs.ai.engine.terminal_node_QMARK_(board,depth,win_length))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tictactoe.cljs.ai.strategies.evaluate_position(board,mark,win_length),null], null);
} else {
return tictactoe.cljs.ai.engine.alpha_beta_search(board,tictactoe.cljs.ai.strategies.order_moves(tictactoe.cljs.ai.strategies.get_available_moves(board),cljs.core.count(board)),depth,is_maximizing,mark,win_length,alpha,beta);
}
});
/**
 * Get the best move for the current board position
 */
tictactoe.cljs.ai.engine.get_best_move = (function tictactoe$cljs$ai$engine$get_best_move(board,mark,win_length){
var map__22984 = tictactoe.cljs.config.settings.get_config(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ai","ai",760454697)], null));
var map__22984__$1 = cljs.core.__destructure_map(map__22984);
var max_depth = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22984__$1,new cljs.core.Keyword(null,"max-depth","max-depth",127060793));
var infinity = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22984__$1,new cljs.core.Keyword(null,"infinity","infinity",-105926847));
var neg_infinity = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22984__$1,new cljs.core.Keyword(null,"neg-infinity","neg-infinity",-776629436));
var winning_move = tictactoe.cljs.ai.strategies.find_winning_move(board,mark,win_length);
var blocking_move = tictactoe.cljs.ai.strategies.find_blocking_move(board,mark,win_length);
if(cljs.core.truth_(winning_move)){
return winning_move;
} else {
if(cljs.core.truth_(blocking_move)){
return blocking_move;
} else {
var vec__22987 = tictactoe.cljs.ai.engine.alpha_beta_search(board,tictactoe.cljs.ai.strategies.order_moves(tictactoe.cljs.ai.strategies.get_available_moves(board),cljs.core.count(board)),max_depth,true,mark,win_length,neg_infinity,infinity);
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22987,(0),null);
var move = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22987,(1),null);
return move;

}
}
});

//# sourceMappingURL=tictactoe.cljs.ai.engine.js.map
