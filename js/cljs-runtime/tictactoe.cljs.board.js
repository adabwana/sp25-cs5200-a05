goog.provide('tictactoe.cljs.board');
/**
 * Initialize an empty board of the specified size.
 */
tictactoe.cljs.board.init_board = (function tictactoe$cljs$board$init_board(size){
return cljs.core.vec(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(size,cljs.core.vec(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(size," "))));
});
/**
 * Check if a move is valid (within bounds and space is empty).
 */
tictactoe.cljs.board.valid_move_QMARK_ = (function tictactoe$cljs$board$valid_move_QMARK_(board,row,col){
return ((((((-1) < row)) && ((row < cljs.core.count(board))))) && (((((((-1) < col)) && ((col < cljs.core.count(board))))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(" ",cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(board,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [row,col], null)))))));
});
/**
 * Place a mark on the board at the specified position.
 */
tictactoe.cljs.board.place_mark = (function tictactoe$cljs$board$place_mark(board,row,col,mark){
return cljs.core.assoc_in(board,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [row,col], null),mark);
});
/**
 * Check if the board is completely filled.
 */
tictactoe.cljs.board.board_full_QMARK_ = (function tictactoe$cljs$board$board_full_QMARK_(board){
return cljs.core.not_any_QMARK_((function (p1__22750_SHARP_){
return cljs.core.some(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 1, [" ",null], null), null),p1__22750_SHARP_);
}),board);
});
/**
 * Get a specific row from the board.
 */
tictactoe.cljs.board.get_row = (function tictactoe$cljs$board$get_row(board,row){
return cljs.core.nth.cljs$core$IFn$_invoke$arity$2(board,row);
});
/**
 * Get a specific column from the board.
 */
tictactoe.cljs.board.get_col = (function tictactoe$cljs$board$get_col(board,col){
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p1__22751_SHARP_){
return cljs.core.nth.cljs$core$IFn$_invoke$arity$2(p1__22751_SHARP_,col);
}),board);
});
/**
 * Get a diagonal line starting from [row col] in the specified direction.
 */
tictactoe.cljs.board.get_diagonal = (function tictactoe$cljs$board$get_diagonal(board,row,col,p__22757){
var vec__22758 = p__22757;
var dx = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22758,(0),null);
var dy = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22758,(1),null);
var size = cljs.core.count(board);
var r = row;
var c = col;
var result = cljs.core.PersistentVector.EMPTY;
while(true){
if((((r < (0))) || ((((r >= size)) || ((((c < (0))) || ((c >= size)))))))){
return result;
} else {
var G__22822 = (r + dx);
var G__22823 = (c + dy);
var G__22824 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(result,cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(board,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [r,c], null)));
r = G__22822;
c = G__22823;
result = G__22824;
continue;
}
break;
}
});
/**
 * Check if a line contains a winning sequence.
 */
tictactoe.cljs.board.check_line = (function tictactoe$cljs$board$check_line(line,player,win_length){
var len = cljs.core.count(line);
var start = (0);
while(true){
if(((start + win_length) > len)){
return false;
} else {
if(cljs.core.every_QMARK_(((function (start,len){
return (function (p1__22769_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(player,p1__22769_SHARP_);
});})(start,len))
,cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(line,start,(start + win_length)))){
return true;
} else {
var G__22825 = (start + (1));
start = G__22825;
continue;
}
}
break;
}
});
/**
 * Check if the specified player has won.
 */
tictactoe.cljs.board.check_winner = (function tictactoe$cljs$board$check_winner(board,player,win_length){
var size = cljs.core.count(board);
var or__5002__auto__ = cljs.core.some((function (p1__22773_SHARP_){
return tictactoe.cljs.board.check_line(tictactoe.cljs.board.get_row(board,p1__22773_SHARP_),player,win_length);
}),cljs.core.range.cljs$core$IFn$_invoke$arity$1(size));
if(cljs.core.truth_(or__5002__auto__)){
return or__5002__auto__;
} else {
var or__5002__auto____$1 = cljs.core.some((function (p1__22774_SHARP_){
return tictactoe.cljs.board.check_line(tictactoe.cljs.board.get_col(board,p1__22774_SHARP_),player,win_length);
}),cljs.core.range.cljs$core$IFn$_invoke$arity$1(size));
if(cljs.core.truth_(or__5002__auto____$1)){
return or__5002__auto____$1;
} else {
return cljs.core.some((function (p1__22775_SHARP_){
var vec__22785 = p1__22775_SHARP_;
var row = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22785,(0),null);
var col = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22785,(1),null);
return ((tictactoe.cljs.board.check_line(tictactoe.cljs.board.get_diagonal(board,row,col,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(1),(1)], null)),player,win_length)) || (tictactoe.cljs.board.check_line(tictactoe.cljs.board.get_diagonal(board,row,col,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(1),(-1)], null)),player,win_length)));
}),(function (){var iter__5480__auto__ = (function tictactoe$cljs$board$check_winner_$_iter__22788(s__22789){
return (new cljs.core.LazySeq(null,(function (){
var s__22789__$1 = s__22789;
while(true){
var temp__5823__auto__ = cljs.core.seq(s__22789__$1);
if(temp__5823__auto__){
var xs__6383__auto__ = temp__5823__auto__;
var i = cljs.core.first(xs__6383__auto__);
var iterys__5476__auto__ = ((function (s__22789__$1,i,xs__6383__auto__,temp__5823__auto__,or__5002__auto____$1,or__5002__auto__,size){
return (function tictactoe$cljs$board$check_winner_$_iter__22788_$_iter__22790(s__22791){
return (new cljs.core.LazySeq(null,((function (s__22789__$1,i,xs__6383__auto__,temp__5823__auto__,or__5002__auto____$1,or__5002__auto__,size){
return (function (){
var s__22791__$1 = s__22791;
while(true){
var temp__5823__auto____$1 = cljs.core.seq(s__22791__$1);
if(temp__5823__auto____$1){
var s__22791__$2 = temp__5823__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__22791__$2)){
var c__5478__auto__ = cljs.core.chunk_first(s__22791__$2);
var size__5479__auto__ = cljs.core.count(c__5478__auto__);
var b__22793 = cljs.core.chunk_buffer(size__5479__auto__);
if((function (){var i__22792 = (0);
while(true){
if((i__22792 < size__5479__auto__)){
var j = cljs.core._nth(c__5478__auto__,i__22792);
cljs.core.chunk_append(b__22793,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [i,j], null));

var G__22829 = (i__22792 + (1));
i__22792 = G__22829;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__22793),tictactoe$cljs$board$check_winner_$_iter__22788_$_iter__22790(cljs.core.chunk_rest(s__22791__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__22793),null);
}
} else {
var j = cljs.core.first(s__22791__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [i,j], null),tictactoe$cljs$board$check_winner_$_iter__22788_$_iter__22790(cljs.core.rest(s__22791__$2)));
}
} else {
return null;
}
break;
}
});})(s__22789__$1,i,xs__6383__auto__,temp__5823__auto__,or__5002__auto____$1,or__5002__auto__,size))
,null,null));
});})(s__22789__$1,i,xs__6383__auto__,temp__5823__auto__,or__5002__auto____$1,or__5002__auto__,size))
;
var fs__5477__auto__ = cljs.core.seq(iterys__5476__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(size)));
if(fs__5477__auto__){
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(fs__5477__auto__,tictactoe$cljs$board$check_winner_$_iter__22788(cljs.core.rest(s__22789__$1)));
} else {
var G__22831 = cljs.core.rest(s__22789__$1);
s__22789__$1 = G__22831;
continue;
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5480__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(size));
})());
}
}
});
/**
 * Check if there is a winner on the board.
 */
tictactoe.cljs.board.winner_QMARK_ = (function tictactoe$cljs$board$winner_QMARK_(board){
var win_length = (5);
var or__5002__auto__ = tictactoe.cljs.board.check_winner(board,"X",win_length);
if(cljs.core.truth_(or__5002__auto__)){
return or__5002__auto__;
} else {
return tictactoe.cljs.board.check_winner(board,"O",win_length);
}
});

//# sourceMappingURL=tictactoe.cljs.board.js.map
