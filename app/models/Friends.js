var mongoose=require('mongoose');
var Schema=mongoose.Schema;

var FriendsSchema=new Schema({
	facebookTo:{
		id:{type:String,required:true,unique:true}
	}
	facebookFrom:{
		id:{type:String,required:true,uniqe:true}
	}
});

module.exports=mongoose.model('Friends',UserSchema);