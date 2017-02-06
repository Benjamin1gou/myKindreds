SELECT
 MAX(line)
 FROM
  memo
  WHERE
 /*if(USER_ID)start*/
 id = USER_ID
 /*if(USER_ID)end*/
 ;