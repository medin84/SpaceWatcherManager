declare const AJS: any;

export function getAjsContextPath() {
  return getAjsMeta("context-path");
}

export function getAjsMeta(key: string) {
  return AJS.Meta.get(key);
}
