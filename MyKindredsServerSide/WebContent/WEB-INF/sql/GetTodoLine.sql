SELECT
 MAX(line)
 FROM
 /*if(TABLE_NAME)start*/
  todo
  /*if(TABLE_NAME)end*/
  WHERE
 /*if(USER_ID)start*/
 id = USER_ID
 /*if(USER_ID)end*/
 ;