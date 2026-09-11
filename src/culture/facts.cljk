(ns culture.facts
  "Country-level regional-culture catalog for Malawi (MWI) -- national
  dishes, protected products, beverages, crafts, festivals and heritage
  sites, per ADR-2607171400 addendum 2 (cloud-itonami-municipality-
  culture-catalog Wave 1, in com-junkawasaki/root). Sibling namespace to
  `marketentry.facts` / `statute.facts` (ADR-2607141700); city-level
  counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of culture entries."
  {"MWI"
   [{:culture/id "mwi.dish.nsima"
     :culture/name "Nsima"
     :culture/country "MWI"
     :culture/kind :dish
     :culture/summary "Malawian name for the maize-meal staple dish known regionally as ugali; Malawi's nsima culinary tradition was recognized by UNESCO in 2017."
     :culture/url "https://en.wikipedia.org/wiki/Ugali"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mwi.dish.chambo"
     :culture/name "Chambo"
     :culture/country "MWI"
     :culture/kind :dish
     :culture/summary "Widely known bream-like fish from Lake Malawi, one of the most favored fish in Malawian cuisine, per the Cuisine of Malawi article."
     :culture/url "https://en.wikipedia.org/wiki/Cuisine_of_Malawi"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mwi.dish.kondowole"
     :culture/name "Kondowole"
     :culture/country "MWI"
     :culture/kind :dish
     :culture/summary "Sticky staple meal made from cassava flour and water, primarily eaten in northern Malawi, per the Cuisine of Malawi article."
     :culture/url "https://en.wikipedia.org/wiki/Cuisine_of_Malawi"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mwi.beverage.thobwa"
     :culture/name "Thobwa"
     :culture/country "MWI"
     :culture/kind :beverage
     :culture/summary "Fermented Malawian drink made from white maize and millet or sorghum, per the Cuisine of Malawi article."
     :culture/url "https://en.wikipedia.org/wiki/Cuisine_of_Malawi"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mwi.beverage.kachasu"
     :culture/name "Kachasu"
     :culture/country "MWI"
     :culture/kind :beverage
     :culture/summary "African traditional distilled beverage made from maize and other ingredients, produced in several countries including Malawi."
     :culture/url "https://en.wikipedia.org/wiki/Kachasu"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mwi.product.tobacco"
     :culture/name "Malawian tobacco"
     :culture/country "MWI"
     :culture/kind :product
     :culture/summary "Malawi's most important export crop, accounting for 65-80% of exports from 1921-1932 and 175,000 tonnes of production in 2011, per the Agriculture in Malawi article."
     :culture/url "https://en.wikipedia.org/wiki/Agriculture_in_Malawi"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mwi.heritage.lake-malawi-national-park"
     :culture/name "Lake Malawi National Park"
     :culture/country "MWI"
     :culture/kind :heritage
     :culture/summary "National park at the southern end of Lake Malawi, designated a UNESCO World Heritage Site in 1984 primarily for its exceptional fish diversity and evolutionary significance."
     :culture/url "https://en.wikipedia.org/wiki/Lake_Malawi_National_Park"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-mwi culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "MWI"))
                 " MWI entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
