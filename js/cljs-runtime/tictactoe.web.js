goog.provide('tictactoe.web');
if((typeof tictactoe !== 'undefined') && (typeof tictactoe.web !== 'undefined') && (typeof tictactoe.web.web_state !== 'undefined')){
} else {
tictactoe.web.web_state = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(tictactoe.cljs.core.init_game.cljs$core$IFn$_invoke$arity$0());
}
tictactoe.web.styles = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(tictactoe.cljs.xxxconfig.get_config(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"styles","styles",1954480375)], null)));
tictactoe.web.cell_style_memo = cljs.core.memoize((function (mark){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"cell","cell",764245084).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(tictactoe.web.styles)),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(mark," "))?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"hover","hover",-341141711),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"background","background",-863952629),"#f0f0f0"], null)], null):null)], 0));
}));
tictactoe.web.cell_click_handler = cljs.core.memoize((function (row,col,mark,game_over_QMARK_,ai_enabled_QMARK_,current_player,ai_player){
if(((cljs.core.not(game_over_QMARK_)) && (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(mark," ")) && (cljs.core.not((function (){var and__5000__auto____$2 = ai_enabled_QMARK_;
if(cljs.core.truth_(and__5000__auto____$2)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_player,ai_player);
} else {
return and__5000__auto____$2;
}
})())))))){
return (function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(tictactoe.web.web_state,tictactoe.cljs.core.make_move,row,col);
});
} else {
return null;
}
}));
tictactoe.web.cell_component = (function tictactoe$web$cell_component(row,col,mark){
var game_state = cljs.core.deref(tictactoe.web.web_state);
var ai_enabled_QMARK_ = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(game_state,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"config","config",994861415),new cljs.core.Keyword(null,"ai-enabled","ai-enabled",-1594045602)], null));
var current_player = new cljs.core.Keyword(null,"current-player","current-player",-970625153).cljs$core$IFn$_invoke$arity$1(game_state);
var ai_player = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(game_state,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"config","config",994861415),new cljs.core.Keyword(null,"ai-player","ai-player",1861644924)], null));
return reagent.core.create_class.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"component-name","component-name",-1318676056),["Cell-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(row),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(col)].join(''),new cljs.core.Keyword(null,"should-component-update","should-component-update",2040868163),(function (this$,p__23043,p__23044){
var vec__23045 = p__23043;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23045,(0),null);
var ___$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23045,(1),null);
var ___$2 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23045,(2),null);
var old_mark = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23045,(3),null);
var vec__23048 = p__23044;
var ___$3 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23048,(0),null);
var ___$4 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23048,(1),null);
var ___$5 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23048,(2),null);
var new_mark = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23048,(3),null);
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(old_mark,new_mark);
}),new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function (row__$1,col__$1,mark__$1){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"style","style",-496642736),tictactoe.web.cell_style_memo(mark__$1),new cljs.core.Keyword(null,"on-click","on-click",1632826543),tictactoe.web.cell_click_handler(row__$1,col__$1,mark__$1,new cljs.core.Keyword(null,"game-over?","game-over?",432859304).cljs$core$IFn$_invoke$arity$1(game_state),ai_enabled_QMARK_,current_player,ai_player)], null),((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(mark__$1," "))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(mark__$1,"X"))?"#e74c3c":"#3498db")], null)], null),mark__$1], null):null)], null);
})], null));
});
tictactoe.web.board_component = reagent.core.create_class.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"component-name","component-name",-1318676056),"Board",new cljs.core.Keyword(null,"should-component-update","should-component-update",2040868163),(function (this$,old_argv,new_argv){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board","board",-1907017633).cljs$core$IFn$_invoke$arity$1(cljs.core.second(old_argv)),new cljs.core.Keyword(null,"board","board",-1907017633).cljs$core$IFn$_invoke$arity$1(cljs.core.second(new_argv)));
}),new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.Keyword(null,"board","board",-1907017633).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(tictactoe.web.styles))], null),(function (){var iter__5480__auto__ = (function tictactoe$web$iter__23053(s__23054){
return (new cljs.core.LazySeq(null,(function (){
var s__23054__$1 = s__23054;
while(true){
var temp__5823__auto__ = cljs.core.seq(s__23054__$1);
if(temp__5823__auto__){
var xs__6383__auto__ = temp__5823__auto__;
var row = cljs.core.first(xs__6383__auto__);
var iterys__5476__auto__ = ((function (s__23054__$1,row,xs__6383__auto__,temp__5823__auto__){
return (function tictactoe$web$iter__23053_$_iter__23055(s__23056){
return (new cljs.core.LazySeq(null,((function (s__23054__$1,row,xs__6383__auto__,temp__5823__auto__){
return (function (){
var s__23056__$1 = s__23056;
while(true){
var temp__5823__auto____$1 = cljs.core.seq(s__23056__$1);
if(temp__5823__auto____$1){
var s__23056__$2 = temp__5823__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__23056__$2)){
var c__5478__auto__ = cljs.core.chunk_first(s__23056__$2);
var size__5479__auto__ = cljs.core.count(c__5478__auto__);
var b__23058 = cljs.core.chunk_buffer(size__5479__auto__);
if((function (){var i__23057 = (0);
while(true){
if((i__23057 < size__5479__auto__)){
var col = cljs.core._nth(c__5478__auto__,i__23057);
var mark = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board","board",-1907017633).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(tictactoe.web.web_state)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [row,col], null));
cljs.core.chunk_append(b__23058,cljs.core.with_meta(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [tictactoe.web.cell_component,row,col,mark], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(row),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(col)].join('')], null)));

var G__23073 = (i__23057 + (1));
i__23057 = G__23073;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__23058),tictactoe$web$iter__23053_$_iter__23055(cljs.core.chunk_rest(s__23056__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__23058),null);
}
} else {
var col = cljs.core.first(s__23056__$2);
var mark = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board","board",-1907017633).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(tictactoe.web.web_state)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [row,col], null));
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [tictactoe.web.cell_component,row,col,mark], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(row),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(col)].join('')], null)),tictactoe$web$iter__23053_$_iter__23055(cljs.core.rest(s__23056__$2)));
}
} else {
return null;
}
break;
}
});})(s__23054__$1,row,xs__6383__auto__,temp__5823__auto__))
,null,null));
});})(s__23054__$1,row,xs__6383__auto__,temp__5823__auto__))
;
var fs__5477__auto__ = cljs.core.seq(iterys__5476__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(tictactoe.web.web_state),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"config","config",994861415),new cljs.core.Keyword(null,"board-size","board-size",140730505)], null)))));
if(fs__5477__auto__){
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(fs__5477__auto__,tictactoe$web$iter__23053(cljs.core.rest(s__23054__$1)));
} else {
var G__23074 = cljs.core.rest(s__23054__$1);
s__23054__$1 = G__23074;
continue;
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5480__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(tictactoe.web.web_state),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"config","config",994861415),new cljs.core.Keyword(null,"board-size","board-size",140730505)], null))));
})()], null);
})], null));
tictactoe.web.controls_component = reagent.core.create_class.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"component-name","component-name",-1318676056),"Controls",new cljs.core.Keyword(null,"should-component-update","should-component-update",2040868163),(function (this$,old_argv,new_argv){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.second(old_argv),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"config","config",994861415),new cljs.core.Keyword(null,"ai-enabled","ai-enabled",-1594045602)], null)),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.second(new_argv),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"config","config",994861415),new cljs.core.Keyword(null,"ai-enabled","ai-enabled",-1594045602)], null)));
}),new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.Keyword(null,"controls","controls",1340701452).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(tictactoe.web.styles))], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button","button",1456579943),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.Keyword(null,"button","button",1456579943).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(tictactoe.web.styles)),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.reset_BANG_(tictactoe.web.web_state,tictactoe.cljs.core.init_game.cljs$core$IFn$_invoke$arity$0());
})], null),"New Game"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button","button",1456579943),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.Keyword(null,"button","button",1456579943).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(tictactoe.web.styles)),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(tictactoe.web.web_state,cljs.core.update_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"config","config",994861415),new cljs.core.Keyword(null,"ai-enabled","ai-enabled",-1594045602)], null),cljs.core.not);
})], null),(cljs.core.truth_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(tictactoe.web.web_state),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"config","config",994861415),new cljs.core.Keyword(null,"ai-enabled","ai-enabled",-1594045602)], null)))?"Disable AI":"Enable AI")], null)], null);
})], null));
tictactoe.web.status_component = reagent.core.create_class.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"component-name","component-name",-1318676056),"Status",new cljs.core.Keyword(null,"should-component-update","should-component-update",2040868163),(function (this$,old_argv,new_argv){
var old_state = cljs.core.second(old_argv);
var new_state = cljs.core.second(new_argv);
return ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"game-over?","game-over?",432859304).cljs$core$IFn$_invoke$arity$1(old_state),new cljs.core.Keyword(null,"game-over?","game-over?",432859304).cljs$core$IFn$_invoke$arity$1(new_state))) || (((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"winner","winner",714604679).cljs$core$IFn$_invoke$arity$1(old_state),new cljs.core.Keyword(null,"winner","winner",714604679).cljs$core$IFn$_invoke$arity$1(new_state))) || (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"current-player","current-player",-970625153).cljs$core$IFn$_invoke$arity$1(old_state),new cljs.core.Keyword(null,"current-player","current-player",-970625153).cljs$core$IFn$_invoke$arity$1(new_state))))));
}),new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(tictactoe.web.styles))], null),(cljs.core.truth_(new cljs.core.Keyword(null,"game-over?","game-over?",432859304).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(tictactoe.web.web_state)))?(function (){var temp__5821__auto__ = new cljs.core.Keyword(null,"winner","winner",714604679).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(tictactoe.web.web_state));
if(cljs.core.truth_(temp__5821__auto__)){
var winner = temp__5821__auto__;
return ["Player ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(winner)," wins!"].join('');
} else {
return "Game is a draw!";
}
})():["Current player: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"current-player","current-player",-970625153).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(tictactoe.web.web_state))),(cljs.core.truth_((function (){var and__5000__auto__ = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(tictactoe.web.web_state),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"config","config",994861415),new cljs.core.Keyword(null,"ai-enabled","ai-enabled",-1594045602)], null));
if(cljs.core.truth_(and__5000__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"current-player","current-player",-970625153).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(tictactoe.web.web_state)),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(tictactoe.web.web_state),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"config","config",994861415),new cljs.core.Keyword(null,"ai-player","ai-player",1861644924)], null)));
} else {
return and__5000__auto__;
}
})())?" (AI's turn)":null)].join('')
)], null);
})], null));
tictactoe.web.game_component = reagent.core.create_class.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"component-name","component-name",-1318676056),"Game",new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"display","display",242065432),"flex",new cljs.core.Keyword(null,"flex-direction","flex-direction",364609438),"column",new cljs.core.Keyword(null,"align-items","align-items",-267946462),"center",new cljs.core.Keyword(null,"font-family","font-family",-667419874),"sans-serif"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"h1","h1",-1896887462),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"color","color",1011675173),"#2c3e50",new cljs.core.Keyword(null,"margin-bottom","margin-bottom",388334941),"20px"], null)], null),"Tic Tac Toe"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tictactoe.web.board_component,cljs.core.deref(tictactoe.web.web_state)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tictactoe.web.status_component,cljs.core.deref(tictactoe.web.web_state)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tictactoe.web.controls_component,cljs.core.deref(tictactoe.web.web_state)], null)], null);
})], null));
tictactoe.web.mount_root = (function tictactoe$web$mount_root(){
var root_el = document.getElementById("app");
return reagent.dom.render.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [tictactoe.web.game_component], null),root_el);
});
tictactoe.web.init_BANG_ = (function tictactoe$web$init_BANG_(){
return tictactoe.web.mount_root();
});

//# sourceMappingURL=tictactoe.web.js.map
