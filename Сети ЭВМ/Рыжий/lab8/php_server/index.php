<?php
    header('Content-Type: application/json; charset=utf-8');
    header('Access-Control-Allow-Origin: *');
    $request = strtok($_SERVER['REQUEST_URI'], '?');

    class MyDB extends SQLite3
    {
        function __construct()
        {
            $this->open('database.db');
        }
    }

    $db = new MyDB();

    switch ($request) {
        case '/scores' : {
            if ($_SERVER['REQUEST_METHOD'] != 'POST') {
                http_response_code(405);
                return;
            }
            
            $score = $_REQUEST['score'];
            $username = $_REQUEST['username'];
            if (is_null($score) or !(is_numeric($score)) or is_null($username)) {
                http_response_code(400);
                return;
            }

            $placeRes = $db->query("SELECT COUNT(*) place FROM scores WHERE score >= '${score}'");
            $place = ($placeRes->fetchArray())['place'] + 1;
            $t = 1;
            $resArray = array(); 
            $flag = TRUE;
            $topRes = $db->query('SELECT * FROM scores ORDER BY scores.score DESC LIMIT 10');
            while ($row = $topRes->fetchArray()) {
                if ((($t == 10) or ($t == $place)) && $flag == TRUE) {
                    $obj = new stdClass();
                    $obj->score = intval($score);
                    $obj->username = $username;
                    $obj->place = $place;
                    $obj->me = TRUE;
                    $flag = FALSE;
                    array_push($resArray, $obj);    
                }

                $obj = new stdClass();
                $obj->score = $row['score'];
                $obj->username = $row['name'];
                $obj->place = $t;
                array_push($resArray, $obj);
                $t += 1;
            }
            array_pop($resArray);

            $db->query("INSERT INTO scores (score, name) VALUES ('$score', '$username')");

            echo json_encode($resArray);
            http_response_code(200);
            return;
        }
        default:
            http_response_code(404);
    }

?>