# Hikingbook
hikingbook test project

### 架構
我使用的是MVVM融合MVI的設計，會使用這種設計的原因可以從MVVM和MVI兩者分開來說
**MVVM**：MVVM是我在Google Codelabs的課程學習時，引導我最深的官方架構，所以對我來說也最熟悉。
而且使用ViewModel來處理UI和數據，真的是一種非常高效的開發架構，Activity肥大問題徹底解決。
**MVI**：使用MVI的原因是因為Compose，Compose的純函式特性最大的特徵就是無狀態，而MVI的單向數據流可以簡單地把狀態提升到外部管理(這邊用的是ViewModel)，可以讓UI的邏輯一致性更強(相較雙向綁定的兩邊數據互相控制的複雜性)，我認為這是最契合Compose的架構
但因為我用的MVI並不是最標準的，舉個例子:使用UiState來封裝整個狀態一起處理，這一點我就沒有實現，一部分是我不熟，另一部分是在UiState真的非常大之前，這樣的作法有點過度設計了。


### 使用的第三方庫
**Retrofit**：最標準的Android Restful API網錄請求庫
**Gson**：標準的Json解析庫
**Google** Map：簡單且功能強大的Map SDK


### 未使用的第三方庫
**Moshi**：之前用過但出現問題，所以這次時間不夠不敢用
**Volley**：大家好像都用Retrofit，而且Retrofit功能比較齊全，只用Volley感覺有點扣分哈哈
**Navigation(Compose)**：這是個蠻有趣的故事，之前我在寫另一隻App的時候，我就是用了ViewModel + Hilt + Jetpakc Compose + Navigation的組合，想說這應該是當前最新最推薦的寫法了吧，結果使用時一直遇到Bug，發現只要使用Navigation後ViewModel就會recreate，找了很久也沒發現寫法上的問題，到最後搜了下發現真的不是我的問題，而是官方組件自己的Bug，issueTracker如下
https://issuetracker.google.com/u/1/issues/265838050

### 其他使用的庫
**Hilt**：官方推薦依賴注入框架，使用的原因是因為很討厭ViewModel帶構造參數要初始化時，要寫Factory甚麼之類的，真的很不優雅，用Hilt後只要簡單寫這樣就好了，舒服多了
    private val viewModel: MainViewModel by viewModels()
**Paging**：想說要求無限滑動的話，還是要用上Paging比較好，可以自動處理載入問題，不過這個比較不熟，用第二次而已
**ML Kit**：想說Quote API回傳都是英文，來加個中文翻譯好了，所以就把之前用過的機器學習庫拿來用，這個庫也是Google官方的
**qichuan**：這個是三方庫，華語之間的翻譯用的，因為ML Kit沒辦法翻譯繁體中文，所以只好先翻譯簡體再用簡體翻成繁體

### 最難的部分
每個庫的版本處理，常常沒用好連編譯都過不了，而且近期Compose和Kotlin都一直更新，所以相容性問題更大...
最後快速的處理方法是拿舊project用的版本來套，不然我不確定要處理多久

### 花了多長時間
大約十個小時左右，因為對Compsoe這些庫已經很熟了，所以寫得還算快
如果可以重新開始，我應該會好好整理每個庫的版本問題，最好做個筆記之類的
還有另一部分是UI，有些UI部分可以重用，但因為是邊寫邊想，導致重用性和模組化不夠，這點覺得小可惜
可以重來的話，會先確定需求再想架構，最後才是寫code

### 其他小心思
我用了很多的過度動畫來讓整個App的使用體驗更美好，還加了Map SDK來讓選位置更輕鬆，UI部分也使用很多Material 3的組件，加上配合動態調整顏色等等，我覺得UI整體上還蠻美的。而且還有加上一些點及空白處收起鍵盤等等的小功能。
而且點擊Quote第一次可以翻譯，第二次可以直接搜尋喔

### 沒完成的部分
因為時間真的不太夠，所以有些地方沒有用，像是Room搜尋，或是排序自定義等等，還有StatusBar上的顏色在明亮主題時顯示不對，這部分也還沒改到。
