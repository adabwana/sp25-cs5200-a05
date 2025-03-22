goog.provide('tictactoe.cljs.config.settings');
/**
 * Get configuration by path. Example: (get-config [:default :board-size])
 */
tictactoe.cljs.config.settings.get_config = (function tictactoe$cljs$config$settings$get_config(path){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(tictactoe.cljs.config.constants.game_constants,path);
});
/**
 * Get style configuration by path. Example: (get-style [:board])
 */
tictactoe.cljs.config.settings.get_style = (function tictactoe$cljs$config$settings$get_style(path){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(tictactoe.cljs.config.constants.styles,path);
});
/**
 * Merge custom configuration with default configuration
 */
tictactoe.cljs.config.settings.merge_config = (function tictactoe$cljs$config$settings$merge_config(custom_config){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([tictactoe.cljs.config.settings.get_config(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"default","default",-1987822328)], null)),custom_config], 0));
});

//# sourceMappingURL=tictactoe.cljs.config.settings.js.map
