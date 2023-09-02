import puppeteer from "puppeteer";

(async () => {
	const browser = await puppeteer.launch({ headless: "new" });
	const page = await browser.newPage();
	await page.goto("http://localhost:5173/");
	await page.screenshot({ path: "out/pic.png" });
	await page.close();
	await browser.close();
})();
