# PicturesLike
Приложение предназначено для просмотра картинок с сервиса https://picsum.photos/ и сохранения понравившихся на устройство.

Приложение работает в системе Android с SDK версий 26+, написано на языке Kotlin.

Для обращения к сервису используется библиотека Retrofit2 и gson для конвертации получаемых данных из JSON в pojo. Хранение данных реализованно в локальной БД смартфона SqlLite. Для управления БД и данными используется библиотека Room. Для обработки асинхронных операций используется JavaRX3. Архитектура приложения построена по трехслойной модели MVVM с использованием LiveData и Dagger2 для Dependency Injection. Разметка интерфейса постороена на XML.
Используется подход SingleActivity c использованием фрагментов.

О работе приложения: 
Приложение имеет один экран с двумя вкладками, которые переключают фрагменты: Картинки и Лайки.
При запуске приложения открывается первая вкладка, и автоматически подгружаются 10 картинок. Каждую картинку можно отметить звездочкой, она сохранится на устройство, а данные об авторе и идентификатор сохранятся в локальную базу данных. Сняв звездочку, удаляем информацию с устройства. Достигнув конца страницы, для загрузки следующей страницы нужно нажать на кнопку More и дождаться загрузки еще 10 картинок.
На второй вкладке отображаются уже сохраненные на устройстве картинки.
