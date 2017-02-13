SELECT MAX(line)
 FROM schedule
 WHERE
 /*if(USER_ID)start*/
 id = USER_ID
 /*if(USER_ID)end*/
 ;