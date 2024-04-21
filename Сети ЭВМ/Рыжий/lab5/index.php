<?php
  $field_res = "Попробуйте угадать загаданное число!";
  $field_att = "";

  $input_field_hdn = "";
  $input_field_att = "";

  $p_attempts = "";
  $p_a = 0;
  $p_b = 10;
  $p_num = 0;
  $use_false = "readonly=\"readonly\"";
  $btn_disable = $use_false;
  $curr_attempts = 0;
  $required_num = 0;
  $game_end = false;
  $is_correct_input = false;

  $crypto_key = "mykey";


  // crypto end

  function init_game() {
    global $input_field_hdn, $input_field_att, $required_num, $curr_attempts, $p_a, $p_b;
    $required_num = rand($p_a, $p_b);
    $curr_attempts = floor(log($p_b - $p_a + 1) + 1);
    $input_field_hdn = $required_num;
    $input_field_att = $curr_attempts;
  }

  function check_bounds($a, $b, $num) {
    global $field_res, $field_att, $p_a, $p_b, $p_num;

    if ($num == ""){
        $field_res = "Пожалуйста, введите число!";
        return false;
    }

    if ($a == "" or $b == "") {
      $field_res = "Границы не должны быть пустыми!";
      return false;
    }

    $val_a = "";
    $val_b = "";
    $val_num = "";

    if (is_numeric($a) && is_numeric($b) && is_numeric($num)) {
      $val_a = intval($a);
      $val_b = intval($b);
      $val_num = intval($num);

    } 
    else {
      $field_res = "Некорретные границы!";
      return false;
    }

    if ($val_a == $val_b) {
        $field_res = "Числа не должны быть равными!";
        return false;
    }

    if ($val_a > $val_b) {
      $field_res = "Начальная граница не может превышать конечную границу!";
      return false;      
    }
    
    if ($val_num < $val_a or $val_num > $val_b) {
      $field_res = "Введенное число вне заданного диапазона!";
      $field_att = "";
      return false;
    }

    $p_a = $val_a;
    $p_b = $val_b;
    $p_num = $val_num;
    return true;
  }

  function crypto($string, $key) {
    $str_len = strlen($string);
    $key_len = strlen($key);
    for($i = 0; $i < $str_len; $i++) {
        $string[$i] = $string[$i] ^ $key[$i % $key_len];
    }
    return $string;
  }

  function encrypt_string($str) {
    global $crypto_key;
    return base64_encode(crypto(strval($str), $crypto_key));
  }

  function decrypt_string($str) {
    global $crypto_key;
    return crypto(base64_decode(strval($str)), $crypto_key);
  }

  function check_hidden_fields($h_num, $h_att) {
    global $required_num, $curr_attempts, $field_res, $game_end, $p_a, $p_b;

    if ($h_num == "" and $h_att == "") {
        init_game();
        return true;
    }

    $d_h_num = ($h_num);
    $d_h_att = ($h_att);

    $required_num = intval(decrypt_string($d_h_num));
    $curr_attempts = intval(decrypt_string($d_h_att));

    if (!is_numeric($required_num) && !is_numeric($curr_attempts)) {
      $field_res = "Данные повреждены! Пожалуйста, начните новую игру!";
      $game_end = true;
      return false;
    }

    $att_test = floor(log($p_b - $p_a + 1) + 1);
    if ($required_num > $p_b or $required_num < $p_a or $curr_attempts < 0 or $curr_attempts > $att_test) {
      $field_res = "Данные повреждены! Пожалуйста, начните новую игру!";
      $game_end = true;
      return false;
    }
    return true;
  }

  function start_game() {
    global $field_att, $field_res, $game_end, $input_field_hdn, $input_field_att, $curr_attempts, $p_num, $required_num, $game_end;
    $game_end = false;

    $field_att = "Осталось попыток: " . $curr_attempts;

    if ($p_num > $required_num) {
      $field_res = "Загаданное число меньше " . $p_num;
    }
    elseif ($p_num < $required_num) {
      $field_res = "Загаданное число больше " . $p_num;
    }
    else {
      $field_res = "Поздравляю! Вы победили!";
      $game_end = true;
      return true;
    }

    $curr_attempts--;
    $field_att = "Осталось попыток: " . $curr_attempts;

    if ($curr_attempts <= 0) {
      $field_res = "Вы проиграли! Правильный ответ: " . $required_num;
      $game_end = true;
      return true;
    }

    $input_field_hdn = $required_num;
    $input_field_att = $curr_attempts;
  }

  function game() {
    $post_a = $_POST['a'];
    $post_b = $_POST['b'];
    $post_num = $_POST['input_num'];

    global $is_correct_input;
    $is_correct_input = check_bounds($post_a, $post_b, $post_num);

    if (!$is_correct_input) {
      return;
    }

    $post_hdn = $_POST['hdn'];
    $post_attempts = $_POST['attempts'];

    if (!check_hidden_fields($post_hdn, $post_attempts)) {
      return;
    }
    start_game();
  }

  // main func
  game();

  if ($game_end) {
    global  $input_field_hdn,  $input_field_att, $p_a, $p_b;
    $input_field_hdn = "";
    $input_field_att = "";
    $p_a = 0;
    $p_b = 10;
  }
  if (!$is_correct_input) {
    $use_false = "";
  } 
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="css/bootstrap.min.css">
  <link rel="stylesheet" href="css/style.css">
  <title>Угадай число</title>
</head>
<body>
  <section class="main-content">
    <div class="container">
      <div class="row">     
        <div class="col">
          <h1>Угадай число (php)</h1>
          <div class="rules">
            <p>Правила игры:</p>
            <ul>
              <li>Введите числа [A, B].</li>
              <li>Компьютер загадает число в данном диапазоне;</li>
              <li>Попробуйте угадать это число;</li>
              <li>Количество попыток ограничено.</li>
            </ul>
          </div>  
        </div>    
      </div>      
      <!-- settings -->
      <div class="forms">
        <form action="index.php" class="row" method="POST">
          <div class="col-lg-6 col-xs-12">
            <label for="input-a" class="form-label">Введите A</label>
            <input type="number" name="a"  class="form-control input" <?php echo $use_false; ?> onKeyPress="numbersOnly()" value="<?php echo $p_a; ?>"  id="input-a">
          </div>
          <div class="col-lg-6 col-xs-12">
            <label for="input-b"class="form-label">Введите B</label>
            <input type="number" name="b"  step="1"  class="form-control input" <?php echo $use_false; ?> onKeyPress="numbersOnly()" value="<?php echo $p_b; ?>" id="input-b">
          </div>
          <div class="col-12">
            <label for="input-num" class="form-label">Попробуйте угадать число!</label>
            <input type="number" name="input_num" class="form-control input" <?php if($game_end == true) { echo $btn_disable; } ?> onKeyPress="numbersOnly()" id="input-num">
            <input type="hidden" name="hdn" class="form-control input" id="input-num" value="<?php echo encrypt_string($input_field_hdn); ?>">
            <input type="hidden" name="attempts" class="form-control input" id="input-num" value="<?php echo encrypt_string($input_field_att); ?>">
            <input type="submit" class="btn-block button-start button-color-start" id="start" value="<?php if($game_end) {echo "Новая игра"; } else { echo "Угадать"; } ?>"> 
          </div>
        </form>
      </div>
      <div class="results" class="hidden" >
        <div class="row">
          <div class="col-12">
            <div class="results" id="result-block">
              <h2 class="result"><?php echo $field_res; ?></h2>
              <h2 class="attempts"><?php echo $field_att; ?></h2>
            </div>
          </div>
        </div>  
      </div>  
    </div> 
  </section>
  <script src="js/main.js"></script>
</body>
</html>