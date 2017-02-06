DELETE
 FROM todo
 WHERE
 /*if(_ID)start*/
 id = _ID
 /*if(_ID)end*/
 AND
 /*if(_LINE)start*/
 line = _LINE
 /*if(_LINE)end*/
 ;