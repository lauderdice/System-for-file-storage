# Upload systém

[@prespjan](https://gitlab.fel.cvut.cz/prespjan)\
[@macurvit](https://gitlab.fel.cvut.cz/macurvit)

## Hlavní funkce
 - Možnost registrace a přihlášení uživatelů
 - Upload omezeného množství souborů a na omezenou dobu zdarma (např. max celkem do 1GB, max na měsíc, po této době se soubory smažou)
 - Uživatelská administrace s přehledem nahraných souborů a možností každý z nich smazat/prodloužit na delší dobu.
 - Placené služby pro nahrání většího souboru, případně pro uschování po delší dobu
 - Placení je řešeno na externím webu: účet na tomto upload systému lze propojit s účtem na webu [Hicoria.com](https://hicoria.com)
 - V případě potřeby placené služby se přes API ověří dostatečný zůstatek na účtu hicoria.com a dle toho se akce povolí nebo zakáže


 Propojení s účtem na webu hicoria.com bude řešeno zasláním požadavku z webu hicoria.com na nový upload systém následujícím způsobem:
  - uživatel musí být přihlášen na webu hicoria.com
  - na webu hicoria.com zadá uživatelské jméno, které má na upload systému, a dojde k ověření existence daného účtu a případnému propojení
  - při požadavku na platbu v upload systému se upload systém dotáže webu hicoria.com na finanční stav propojeného účtu, pokud je dostatečný, odečte se potřebná částka z účtu na hicoria.com

Pod pojmem "propojení" si zde představujeme jen uložení ID z webu hicoria.com v databázi upload systému, aby bylo možné nějak identifikovat účet a jeho zůstatek.
Komunikace mezi oběma weby by byla možná samozřejmě pouze s ověřením (token vygenerován na hicoria.com, následně uložen na upload systému).

Systém je určen pro zákazníky firmy hicoria.com, bude sloužit jako spolehlivé krátkodobé (v případě placení klidně dlouhodobé) úložiště souborů, především  pro možnost sdílení těcho souborů s dalšími lidmi.

> Zadání jsme vymysleli na poslední chvíli, a nebylo tedy v době odevzdání prodiskutováno se cvičícím, případné změny nahraji co nejdříve jako novou verzi readme.