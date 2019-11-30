<template>
    <div class="mavonEditor">
        <mavon-editor v-model="handbook"
                      :subfield="props.subfield"
                      :defaultOpen="props.defaultOpen"
                      :editable="props.editable"
                      :toolbarsFlag="props.toolbarsFlag"
                      :navigation="props.navigation"
                      :boxShadow="props.boxShadow"/>

    </div>
</template>

<style scoped>
    .mavonEditor {
        margin-top: 30px;
        width: 100%;
        height: 100%;
    }
</style>

<script>

    export default {
        name: 'home',
        data() {
            return {
                props: {
                    defaultOpen: 'preview',
                    subfield: false,
                    editable: true,
                    toolbarsFlag: true,
                    navigation: true,
                    boxShadow: false
                },
                handbook: "",
            };
        },
        created() {
            fetch(this.SERVER_API_URL + "/home", {
                headers: {
                    "Content-Type": "application/json; charset=UTF-8"
                },
                method: "GET",
            }).then(response => response.json())
                .then(json => {
                    this.handbook = json.homeMessage;
                });
        }
    }
</script>
