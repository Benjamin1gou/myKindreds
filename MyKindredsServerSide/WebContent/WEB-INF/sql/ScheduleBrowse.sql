SELECT id, content
 FROM schedule
 WHERE
 /*if(USER_ID)start*/
 id = USER_ID
 /*if(USER_ID)end*/
 AND
 /*if(_DATE)start*/
 date = _DATE
 /*if(_DATE)end*/
;