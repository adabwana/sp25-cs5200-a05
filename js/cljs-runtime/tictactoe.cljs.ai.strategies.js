goog.provide('tictactoe.cljs.ai.strategies');
tictactoe.cljs.ai.strategies.evaluate_position = cljs.core.memoize((function (board,mark,win_length){
var map__22841 = tictactoe.cljs.config.settings.get_config(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ai","ai",760454697)], null));
var map__22841__$1 = cljs.core.__destructure_map(map__22841);
var win_score = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22841__$1,new cljs.core.Keyword(null,"win-score","win-score",-881459403));
var lose_score = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22841__$1,new cljs.core.Keyword(null,"lose-score","lose-score",-842458103));
var draw_score = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22841__$1,new cljs.core.Keyword(null,"draw-score","draw-score",1455733681));
if(cljs.core.truth_(tictactoe.cljs.board.check_winner(board,mark,win_length))){
return win_score;
} else {
if(cljs.core.truth_(tictactoe.cljs.board.check_winner(board,((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(mark,"X"))?"O":"X"),win_length))){
return lose_score;
} else {
if(tictactoe.cljs.board.board_full_QMARK_(board)){
return draw_score;
} else {
return (0);

}
}
}
}));
/**
 * Get all available moves on the board using transients for better performance
 */
tictactoe.cljs.ai.strategies.get_available_moves = (function tictactoe$cljs$ai$strategies$get_available_moves(board){
return cljs.core.persistent_BANG_(cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (moves,row){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (acc,col){
if(tictactoe.cljs.board.valid_move_QMARK_(board,row,col)){
return cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(acc,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [row,col], null));
} else {
return acc;
}
}),moves,cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.count(board)));
}),cljs.core.transient$(cljs.core.PersistentVector.EMPTY),cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.count(board))));
});
tictactoe.cljs.ai.strategies.center_distance = cljs.core.memoize((function (p__22871,board_size){
var vec__22874 = p__22871;
var row = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22874,(0),null);
var col = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22874,(1),null);
var center = cljs.core.quot(board_size,(2));
return (Math.abs((row - center)) + Math.abs((col - center)));
}));
tictactoe.cljs.ai.strategies.move_scores_cache = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
/**
 * Pre-calculate scores for all possible moves on a board size using transients
 */
tictactoe.cljs.ai.strategies.calculate_move_scores = (function tictactoe$cljs$ai$strategies$calculate_move_scores(board_size){
return cljs.core.persistent_BANG_(cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (scores,pos){
var vec__22884 = pos;
var row = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22884,(0),null);
var col = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22884,(1),null);
var center = cljs.core.quot(board_size,(2));
var dist = tictactoe.cljs.ai.strategies.center_distance(pos,board_size);
var is_center_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(pos,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [center,center], null));
var is_corner_QMARK_ = ((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(row,(0))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(row,(board_size - (1)))))) && (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(col,(0))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(col,(board_size - (1)))))));
return cljs.core.assoc_BANG_.cljs$core$IFn$_invoke$arity$3(scores,pos,((is_center_QMARK_)?(0):((is_corner_QMARK_)?(dist + (1)):(dist + (2))
)));
}),cljs.core.transient$(cljs.core.PersistentArrayMap.EMPTY),(function (){var iter__5480__auto__ = (function tictactoe$cljs$ai$strategies$calculate_move_scores_$_iter__22889(s__22890){
return (new cljs.core.LazySeq(null,(function (){
var s__22890__$1 = s__22890;
while(true){
var temp__5823__auto__ = cljs.core.seq(s__22890__$1);
if(temp__5823__auto__){
var xs__6383__auto__ = temp__5823__auto__;
var row = cljs.core.first(xs__6383__auto__);
var iterys__5476__auto__ = ((function (s__22890__$1,row,xs__6383__auto__,temp__5823__auto__){
return (function tictactoe$cljs$ai$strategies$calculate_move_scores_$_iter__22889_$_iter__22891(s__22892){
return (new cljs.core.LazySeq(null,((function (s__22890__$1,row,xs__6383__auto__,temp__5823__auto__){
return (function (){
var s__22892__$1 = s__22892;
while(true){
var temp__5823__auto____$1 = cljs.core.seq(s__22892__$1);
if(temp__5823__auto____$1){
var s__22892__$2 = temp__5823__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__22892__$2)){
var c__5478__auto__ = cljs.core.chunk_first(s__22892__$2);
var size__5479__auto__ = cljs.core.count(c__5478__auto__);
var b__22894 = cljs.core.chunk_buffer(size__5479__auto__);
if((function (){var i__22893 = (0);
while(true){
if((i__22893 < size__5479__auto__)){
var col = cljs.core._nth(c__5478__auto__,i__22893);
cljs.core.chunk_append(b__22894,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [row,col], null));

var G__22935 = (i__22893 + (1));
i__22893 = G__22935;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__22894),tictactoe$cljs$ai$strategies$calculate_move_scores_$_iter__22889_$_iter__22891(cljs.core.chunk_rest(s__22892__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__22894),null);
}
} else {
var col = cljs.core.first(s__22892__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [row,col], null),tictactoe$cljs$ai$strategies$calculate_move_scores_$_iter__22889_$_iter__22891(cljs.core.rest(s__22892__$2)));
}
} else {
return null;
}
break;
}
});})(s__22890__$1,row,xs__6383__auto__,temp__5823__auto__))
,null,null));
});})(s__22890__$1,row,xs__6383__auto__,temp__5823__auto__))
;
var fs__5477__auto__ = cljs.core.seq(iterys__5476__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(board_size)));
if(fs__5477__auto__){
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(fs__5477__auto__,tictactoe$cljs$ai$strategies$calculate_move_scores_$_iter__22889(cljs.core.rest(s__22890__$1)));
} else {
var G__22938 = cljs.core.rest(s__22890__$1);
s__22890__$1 = G__22938;
continue;
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5480__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(board_size));
})()));
});
tictactoe.cljs.ai.strategies.get_move_scores = cljs.core.memoize((function (board_size){
var temp__5821__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(tictactoe.cljs.ai.strategies.move_scores_cache),board_size);
if(cljs.core.truth_(temp__5821__auto__)){
var scores = temp__5821__auto__;
return scores;
} else {
var scores = tictactoe.cljs.ai.strategies.calculate_move_scores(board_size);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(tictactoe.cljs.ai.strategies.move_scores_cache,cljs.core.assoc,board_size,scores);

return scores;
}
}));
/**
 * Order moves to improve alpha-beta pruning efficiency
 */
tictactoe.cljs.ai.strategies.order_moves = (function tictactoe$cljs$ai$strategies$order_moves(moves,board_size){
var scores = tictactoe.cljs.ai.strategies.get_move_scores(board_size);
return cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2((function (p1__22902_SHARP_){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(scores,p1__22902_SHARP_);
}),moves);
});
/**
 * Check if placing a mark at the given position would create a win
 */
tictactoe.cljs.ai.strategies.would_win_QMARK_ = (function tictactoe$cljs$ai$strategies$would_win_QMARK_(board,row,col,mark,win_length){
if(tictactoe.cljs.board.valid_move_QMARK_(board,row,col)){
var new_board = tictactoe.cljs.board.place_mark(board,row,col,mark);
return tictactoe.cljs.board.check_winner(new_board,mark,win_length);
} else {
return null;
}
});
tictactoe.cljs.ai.strategies.find_winning_move = cljs.core.memoize((function (board,mark,win_length){
var board_size = cljs.core.count(board);
var scores = tictactoe.cljs.ai.strategies.get_move_scores(board_size);
return cljs.core.first((function (){var iter__5480__auto__ = (function tictactoe$cljs$ai$strategies$iter__22906(s__22907){
return (new cljs.core.LazySeq(null,(function (){
var s__22907__$1 = s__22907;
while(true){
var temp__5823__auto__ = cljs.core.seq(s__22907__$1);
if(temp__5823__auto__){
var s__22907__$2 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(s__22907__$2)){
var c__5478__auto__ = cljs.core.chunk_first(s__22907__$2);
var size__5479__auto__ = cljs.core.count(c__5478__auto__);
var b__22909 = cljs.core.chunk_buffer(size__5479__auto__);
if((function (){var i__22908 = (0);
while(true){
if((i__22908 < size__5479__auto__)){
var vec__22910 = cljs.core._nth(c__5478__auto__,i__22908);
var row = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22910,(0),null);
var col = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22910,(1),null);
if(cljs.core.truth_(tictactoe.cljs.ai.strategies.would_win_QMARK_(board,row,col,mark,win_length))){
cljs.core.chunk_append(b__22909,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [row,col], null));

var G__22961 = (i__22908 + (1));
i__22908 = G__22961;
continue;
} else {
var G__22962 = (i__22908 + (1));
i__22908 = G__22962;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__22909),tictactoe$cljs$ai$strategies$iter__22906(cljs.core.chunk_rest(s__22907__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__22909),null);
}
} else {
var vec__22913 = cljs.core.first(s__22907__$2);
var row = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22913,(0),null);
var col = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22913,(1),null);
if(cljs.core.truth_(tictactoe.cljs.ai.strategies.would_win_QMARK_(board,row,col,mark,win_length))){
return cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [row,col], null),tictactoe$cljs$ai$strategies$iter__22906(cljs.core.rest(s__22907__$2)));
} else {
var G__22963 = cljs.core.rest(s__22907__$2);
s__22907__$1 = G__22963;
continue;
}
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5480__auto__(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2((function (p1__22905_SHARP_){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(scores,p1__22905_SHARP_);
}),(function (){var iter__5480__auto____$1 = (function tictactoe$cljs$ai$strategies$iter__22918(s__22919){
return (new cljs.core.LazySeq(null,(function (){
var s__22919__$1 = s__22919;
while(true){
var temp__5823__auto__ = cljs.core.seq(s__22919__$1);
if(temp__5823__auto__){
var xs__6383__auto__ = temp__5823__auto__;
var row = cljs.core.first(xs__6383__auto__);
var iterys__5476__auto__ = ((function (s__22919__$1,row,xs__6383__auto__,temp__5823__auto__,iter__5480__auto__,board_size,scores){
return (function tictactoe$cljs$ai$strategies$iter__22918_$_iter__22920(s__22921){
return (new cljs.core.LazySeq(null,((function (s__22919__$1,row,xs__6383__auto__,temp__5823__auto__,iter__5480__auto__,board_size,scores){
return (function (){
var s__22921__$1 = s__22921;
while(true){
var temp__5823__auto____$1 = cljs.core.seq(s__22921__$1);
if(temp__5823__auto____$1){
var s__22921__$2 = temp__5823__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__22921__$2)){
var c__5478__auto__ = cljs.core.chunk_first(s__22921__$2);
var size__5479__auto__ = cljs.core.count(c__5478__auto__);
var b__22923 = cljs.core.chunk_buffer(size__5479__auto__);
if((function (){var i__22922 = (0);
while(true){
if((i__22922 < size__5479__auto__)){
var col = cljs.core._nth(c__5478__auto__,i__22922);
if(tictactoe.cljs.board.valid_move_QMARK_(board,row,col)){
cljs.core.chunk_append(b__22923,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [row,col], null));

var G__22964 = (i__22922 + (1));
i__22922 = G__22964;
continue;
} else {
var G__22965 = (i__22922 + (1));
i__22922 = G__22965;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__22923),tictactoe$cljs$ai$strategies$iter__22918_$_iter__22920(cljs.core.chunk_rest(s__22921__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__22923),null);
}
} else {
var col = cljs.core.first(s__22921__$2);
if(tictactoe.cljs.board.valid_move_QMARK_(board,row,col)){
return cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [row,col], null),tictactoe$cljs$ai$strategies$iter__22918_$_iter__22920(cljs.core.rest(s__22921__$2)));
} else {
var G__22975 = cljs.core.rest(s__22921__$2);
s__22921__$1 = G__22975;
continue;
}
}
} else {
return null;
}
break;
}
});})(s__22919__$1,row,xs__6383__auto__,temp__5823__auto__,iter__5480__auto__,board_size,scores))
,null,null));
});})(s__22919__$1,row,xs__6383__auto__,temp__5823__auto__,iter__5480__auto__,board_size,scores))
;
var fs__5477__auto__ = cljs.core.seq(iterys__5476__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(board_size)));
if(fs__5477__auto__){
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(fs__5477__auto__,tictactoe$cljs$ai$strategies$iter__22918(cljs.core.rest(s__22919__$1)));
} else {
var G__22977 = cljs.core.rest(s__22919__$1);
s__22919__$1 = G__22977;
continue;
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5480__auto____$1(cljs.core.range.cljs$core$IFn$_invoke$arity$1(board_size));
})()));
})());
}));
/**
 * Find a move that blocks the opponent's win
 */
tictactoe.cljs.ai.strategies.find_blocking_move = (function tictactoe$cljs$ai$strategies$find_blocking_move(board,mark,win_length){
return tictactoe.cljs.ai.strategies.find_winning_move(board,((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(mark,"X"))?"O":"X"),win_length);
});

//# sourceMappingURL=tictactoe.cljs.ai.strategies.js.map
