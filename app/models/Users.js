var mongoose=require('mongoose');
var Schema=mongoose.Schema;

var UserSchema=new Schema({
	name:{type:String,required:true},
	facebook:{
		id:{type:String,required:true,uniqe:true}
	}
});

module.exports=mongoose.model('Users',UserSchema);