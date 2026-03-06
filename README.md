# AI Test Generator – IntelliJ Plugin

Generate **Playwright automation tests instantly from Excel test cases using AI.**

This plugin helps QA engineers and automation developers convert manual test cases into executable automation tests with minimal effort.

---

## ✨ Features

* 📄 **Upload Excel test cases**
* 🤖 **AI converts test cases to Playwright Java tests**
* ⚡ **Generate ready-to-run automation code**
* 🧠 Supports:

  * Playwright Java
  * JUnit 5 assertions
  * Page Object Model structure
* 🧩 Insert generated tests **directly into the editor**

---

## 🚀 How It Works

1. Prepare test cases in Excel.

Example format:

| TestId    | Action                             | Input              | Expected Result          |
| --------- | ---------------------------------- | ------------------ | ------------------------ |
| TC_CP_001 | Verify Product UID auto generation | Create new product | UID should auto generate |

2. Open IntelliJ.

3. Right click inside your test class.

4. Click:

```
Generate Test From Excel
```

5. Upload the Excel file.

6. The plugin generates Playwright test code using AI.

---

## 🧪 Example Generated Test

```java
@Test
void TC_CP_001_verifyProductUIDAutoGeneration() {
    page.navigate("https://your-app-url.com/products/new");

    page.fill("#productName", "Test Product");
    page.click("#createProductButton");

    String productUID = page.textContent("#productUID");

    Assertions.assertNotNull(productUID);
}
```

---

## 📦 Installation

### From JetBrains Marketplace

```
Settings → Plugins → Marketplace
Search: AI Test Generator
Install
```

### Manual Installation

1. Download plugin `.zip`
2. Open:

```
Settings → Plugins → ⚙ → Install plugin from disk
```

---

## ⚙ Requirements

* IntelliJ IDEA 2023+
* Java project
* Playwright dependency (optional but recommended)

---

## 🔐 API Key

This plugin uses OpenAI API to generate tests.

Set environment variable:

```
OPENAI_API_KEY=your_api_key
```

---

## 📌 Roadmap

* [ ] Support Selenium generation
* [ ] Support REST API test generation
* [ ] Prompt customization
* [ ] Test scenario suggestions
* [ ] AI locator improvement

---

## 👩‍💻 Author

Created by **Kanupriya**
Senior Technical Lead – Automation Engineering

---

## ⭐ Contributing

Contributions are welcome.

Open an issue or submit a pull request.

---

## 📄 License

Apache 2.0 License
