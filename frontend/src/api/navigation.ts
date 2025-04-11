let navigator: (path: string) => void;

export const setNavigator = (navFn: (path: string) => void) => {
  navigator = navFn;
};

export const redirect = (path: string) => {
  if (navigator) {
    navigator(path);
  } else {
    console.warn("Navigator not set");
    window.location.href = path;
  }
};